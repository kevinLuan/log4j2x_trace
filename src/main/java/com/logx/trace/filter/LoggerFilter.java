package com.logx.trace.filter;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * 扩展日志过滤器
 * 
 * @author kevin
 *
 */
@Plugin(name = "LoggerFilter", category = "Core", elementType = "appender", printObject = true)
public class LoggerFilter extends AbstractAppender {
	/**
	 * 验证是否激活过滤支持
	 */
	private final boolean enable;// true:false 默认:true

	protected LoggerFilter(String name, Filter filter, Layout<? extends Serializable> layout, boolean enable) {
		super(name, filter, layout);
		this.enable = enable;
	}

	@Override
	public void append(LogEvent event) {
		if (enable) {
			if (this.isFilterLog(event) && this.isMatches(event)) {
				System.out.println("过滤消息：" + event.getMessage().getFormattedMessage());
				if (event.getThrown() != null) {
					System.out.println(event.getThrown());
				}
				if (event.getMessage().getThrowable() != null) {
					System.out.println(event.getMessage().getThrowable());
				}
			}
		}
	}

	/**
	 * 验证是否匹配过滤级别
	 * 
	 * @param event
	 * @return
	 */
	private boolean isFilterLog(LogEvent event) {
		return (event.getLevel().compareTo(Level.ERROR) == 0);
	}

	/**
	 * 验证是否匹配过滤日志
	 * 
	 * @param event
	 * @return
	 */
	private boolean isMatches(LogEvent event) {
		if (event.getMessage().getFormattedMessage() == null) {
			return false;
		}
		// 匹配消息内容为:xx时进行过滤
		return (event.getMessage().getFormattedMessage().equals("xx"));
	}

	@PluginFactory
	public static LoggerFilter createAppender(@PluginAttribute("name") String name,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") final Filter filter, @PluginAttribute("enable") String enable) {
		if (name == null) {
			LOGGER.error("No name provided for HttpRequestDumpLogAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new LoggerFilter(name, filter, layout, !"false".equalsIgnoreCase(enable));
	}
}
