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

import com.logx.tps.limit.TpsLimit;
import com.logx.tps.monitor.LogMonitorFactory;

/**
 * <ul>
 * <li>写入日志限制（磁盘写入保护）</li>
 * <li>支持按写入速率</li>
 * <li>注意这里只对同步写入日志有效，暂不支持异步写入限制支持</li>
 * </ul>
 * 
 * @author KEVIN LUAN
 *
 */
@Plugin(name = "LimitTps", category = "Core", elementType = "filter", printObject = true)
public class LimitTps extends AbstractFilter {
	private final Level level;
	/**
	 * 限制写入日志的速率
	 */
	private final TpsLimit TPS_LIMIT;

	protected LimitTps(final Level level, final Result onMatch, final Result onMismatch, final long limitTPS,
			final int periodTimeMillis) {
		super(onMatch, onMismatch);
		this.level = level;
		TPS_LIMIT = new TpsLimit(limitTPS, periodTimeMillis);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg,
			final Object... params) {
		return filter(level);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg,
			final Throwable t) {
		return filter(level);
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg,
			final Throwable t) {
		return filter(level);
	}

	@Override
	public Result filter(final LogEvent event) {
		return filter(event.getLevel());
	}

	protected Result filter(final Level level) {
		if (TPS_LIMIT.isOverstepTps()) {
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
	public static LimitTps createFilter(@PluginAttribute("level") final Level level, //
			@PluginAttribute("onMatch") final Result match, //
			@PluginAttribute("onMismatch") final Result mismatch, //
			@PluginAttribute("limitTPS") final int limitTPS, /* 获取限制速率 */
			@PluginAttribute("periodTimeMillis") final int periodTimeMillis /* 获取限制速率周期毫秒值 */
	) {
		final Level actualLevel = level == null ? Level.ERROR : level;
		final Result onMatch = match == null ? Result.NEUTRAL : match;
		final Result onMismatch = mismatch == null ? Result.DENY : mismatch;
		assertValue(limitTPS, "Configuration limitTPS is missing or invalid");
		assertValue(periodTimeMillis, "Configuration periodTimeMillis is missing or invalid");
		return new LimitTps(actualLevel, onMatch, onMismatch, limitTPS, periodTimeMillis);
	}

	protected static void assertValue(long value, String errorMsg) {
		if (value <= 0) {
			throw new IllegalArgumentException(errorMsg);
		}
	}
}
