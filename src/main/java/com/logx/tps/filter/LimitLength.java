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

import com.logx.tps.limit.LengthLimit;
import com.logx.tps.monitor.LogMonitorFactory;
import com.logx.utils.Unit;

/**
 * <ul>
 * <li>写入日志限制（磁盘写入保护）</li>
 * <li>支持按写入字符长度限制</li>
 * <li>注意这里只对同步写入日志有效，暂不支持异步写入限制支持</li>
 * </ul>
 * 
 * @author KEVIN LUAN
 *
 */
@Plugin(name = "LimitLength", category = "Core", elementType = "filter", printObject = true)
public class LimitLength extends AbstractFilter {

	private final Level level;
	/**
	 * 限制写入日志的大小
	 */
	private final LengthLimit TPS_LIMIT;

	protected LimitLength(final Level level, final Result onMatch, final Result onMismatch, final long limitLength,
			final int periodTimeMillis) {
		super(onMatch, onMismatch);
		this.level = level;
		TPS_LIMIT = new LengthLimit(limitLength, periodTimeMillis);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg,
			final Object... params) {
		long length = msg.length();
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				length += params[i].toString().length();
			}
		}
		return filter(length);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg,
			final Throwable t) {
		long length = 0;
		if (msg != null) {
			length = msg.toString().length();
		}
		if (t != null) {
			length += t.toString().length();
		}
		return filter(length);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg,
			final Throwable t) {
		long length = 0;
		if (msg != null) {
			length = msg.getFormattedMessage().length();
		}
		if (t != null) {
			length += t.toString().length();
		}
		return filter(length);
	}

	@Override
	public Result filter(final LogEvent event) {
		long length = 0L;
		if (event.getMessage().getFormattedMessage() != null) {
			length = event.getMessage().getFormattedMessage().length();
			if (event.getThrown() != null) {
				length += event.getThrown().toString().length();
			}
		} else {
			length = 4;// null 的字符串长度
		}
		return filter(length);
	}

	protected Result filter(long length) {
		if (TPS_LIMIT.isOverstepTps(length)) {
			// 拒绝输出日志只会会写入log文件受影响，对console不会影响
			LogMonitorFactory.sendMsg(TPS_LIMIT.getMsg());
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
	public static LimitLength createFilter(@PluginAttribute("level") final Level level, //
			@PluginAttribute("onMatch") final Result match, //
			@PluginAttribute("onMismatch") final Result mismatch, //
			@PluginAttribute("limit") final String limit, /* 获取限制周期内写入大小 */
			@PluginAttribute("periodTimeMillis") final int periodTimeMillis /* 获取限制速率周期毫秒值 */
	) {
		final Level actualLevel = level == null ? Level.ERROR : level;
		final Result onMatch = match == null ? Result.NEUTRAL : match;
		final Result onMismatch = mismatch == null ? Result.DENY : mismatch;
		long limitLength = Unit.parser(limit).getBytes();
		assertValue(limitLength, "Configuration limitTPS is missing or invalid");
		assertValue(periodTimeMillis, "Configuration periodTimeMillis is missing or invalid");
		return new LimitLength(actualLevel, onMatch, onMismatch, limitLength, periodTimeMillis);
	}

	protected static void assertValue(long value, String errorMsg) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMsg);
		}
	}
}
