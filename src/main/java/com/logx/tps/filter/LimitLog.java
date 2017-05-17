package com.logx.tps.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import com.logx.tps.limit.Limit;
import com.logx.tps.monitor.LogMonitorFactory;
import com.logx.utils.LogUtils;
import com.logx.utils.Unit;

/**
 * <ul>
 * <li>写入日志限制（磁盘写入保护）</li>
 * <li>支持按写入速率及写入字符长度限制</li>
 * <li>注意这里只对同步写入日志有效，暂不支持异步写入限制支持</li>
 * </ul>
 * 
 * @author KEVIN LUAN
 *
 */
@Plugin(name = "LimitLog", category = "Core", elementType = "filter", printObject = true)
public class LimitLog extends AbstractFilter {
	private final Level level;
	/**
	 * 限制写入日志的速率
	 */
	private final Limit limit;

	protected LimitLog(final Level level, final Result onMatch, final Result onMismatch, final Limit limit) {
		super(onMatch, onMismatch);
		this.level = level;
		this.limit = limit;
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg,
			final Object... params) {
		long length = LogUtils.getLength(msg, null, params);
		return filter(length);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg,
			final Throwable t) {
		long length = LogUtils.getLength(msg, t);
		return filter(length);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg,
			final Throwable t) {
		long length = LogUtils.getLength(msg.getFormattedMessage(), t);
		return filter(length);
	}

	@Override
	public Result filter(final LogEvent event) {

		long length = LogUtils.getLength(event.getMessage().getFormattedMessage(), event.getThrown());
		return filter(length);
	}

	protected Result filter(long length) {
		if (limit.isOverstepLimit(length)) {
			// 拒绝输出日志只会会写入log文件受影响，对console不会影响
			LogMonitorFactory.sendMsg(limit.getMsg());
			return Result.DENY;
		} else {
			return onMatch;
		}
	}

	@Override
	public String toString() {
		return level.toString();
	}

	/**
	 * Create a ThresholdFilter.
	 * 
	 * @param level
	 *            The log Level.
	 * @param match
	 *            The action to take on a match.
	 * @param mismatch
	 *            The action to take on a mismatch.
	 * @return The created ThresholdFilter.
	 */
	@PluginFactory
	public static LimitLog createFilter(@PluginAttribute("level") final Level level, //
			@PluginAttribute("onMatch") final Result match, //
			@PluginAttribute("onMismatch") final Result mismatch, //
			@PluginAttribute("limitTps") final long limitTps, /* 获取限制速率 */
			@PluginAttribute("limitLength") final String limitLength, /* 限制周期内写入长度 */
			@PluginAttribute("periodTimeMillis") final int periodTimeMillis /* 获取限制速率周期毫秒值 */
	) {
		final Level actualLevel = level == null ? Level.ERROR : level;
		final Result onMatch = match == null ? Result.NEUTRAL : match;
		final Result onMismatch = mismatch == null ? Result.DENY : mismatch;
		final long limitLen = parserLimit(limitLength);
		if (limitLen < 1 || limitTps < 1) {
			throw new IllegalArgumentException("`limitTps` and `limitLength` must greater than zero");
		}
		assertValue(periodTimeMillis, "Configuration periodTimeMillis is missing or invalid");
		Limit limit = new Limit(limitTps, limitLen, periodTimeMillis);
		return new LimitLog(actualLevel, onMatch, onMismatch, limit);
	}

	private static long parserLimit(String limitTps) {
		if (limitTps != null) {
			final long value = Unit.parser(limitTps).getBytes();
			if (value > 0) {
				return value;
			}
		}
		return -1;
	}

	protected static void assertValue(long value, String errorMsg) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMsg);
		}
	}
}
