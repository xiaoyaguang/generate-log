package com.mokylin.sink.tools.loggenerator;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.mokylin.sink.tools.loggenerator.component.LogField;
import com.mokylin.sink.tools.loggenerator.component.LogTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 日志模板加载器
 * Created by yaguang.xiao on 2016/9/6.
 */
class LogTemplateLoader {

		static final String OPERATOR_ID_NAME = "operatorID";
		static final String IEVENT_ID = "iEventId";
		static final String IWORLD_ID = "iWorldId";

		enum EventIdGenerateType {
				/** 自动生成 */
				AUTO_GENERATE, /** 传入日志方法 */
				PASS_IN, /** 上面两个方法都生成 */
				AUTO_GENERATE_AND_PASS_IN,
		}

		static List<LogTemplate> loadLogTemplateFromXml(final String path)
						throws ParserConfigurationException, IOException, SAXException {
				File file = new File(path);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);

				doc.getDocumentElement().normalize();

				NodeList structNodes = doc.getElementsByTagName("struct");
				List<LogTemplate> logTemplateList = Lists.newArrayListWithCapacity(structNodes.getLength());
				Set<String> logNames = Sets.newHashSet();
				for (int i = 0; i < structNodes.getLength(); i++) {
						Node structNode = structNodes.item(i);
						if (structNode.getNodeType() == Node.ELEMENT_NODE) {
								LogTemplate logTemplate = loadLogTemplate((Element) structNode);

								if (logNames.contains(logTemplate.logName)) {
										throw new RuntimeException("有重复日志：" + logTemplate.logName);
								}
								logNames.add(logTemplate.logName);

								logTemplateList.add(logTemplate);
						}
				}

				return logTemplateList;
		}

		private static LogTemplate loadLogTemplate(Element structElement) {
				String logName = structElement.getAttribute("name");
				if (Strings.isNullOrEmpty(logName)) {
						throw new RuntimeException("竟然有个日志没有填名字！！！");
				}
				String logDesc = structElement.getAttribute("desc");

				String dontUseSpecialArgStr = structElement.getAttribute("dont_use_special_arg");
				boolean dontUseSpecialArg = false;
				if (!Strings.isNullOrEmpty(dontUseSpecialArgStr)) {
						if (dontUseSpecialArgStr.equals("true")) {
								dontUseSpecialArg = true;
						}
				}

				String eventIdGenerateType = structElement.getAttribute("event_id_generate_type");
				if (Strings.isNullOrEmpty(eventIdGenerateType)) {
						eventIdGenerateType = EventIdGenerateType.AUTO_GENERATE.name();
				}

				boolean valid = false;
				for (EventIdGenerateType idGenerateType : EventIdGenerateType.values()) {
						if (eventIdGenerateType.equals(idGenerateType.name())) {
								valid = true;
								break;
						}
				}
				if (!valid) {
						throw new RuntimeException(
										"event_id_generate_type无效，格式为：不填默认AUTO_GENERATE， AUTO_GENERATE-自" +
														"动生成， PASS_IN-传入日志方法，AUTO_GENERATE_AND_PASS_IN-前面两个方法都" + "生成。日志名称:" +
														logName);
				}

				NodeList entryNodes = structElement.getElementsByTagName("entry");
				List<LogField> fields = Lists.newArrayListWithCapacity(entryNodes.getLength() + 1);
				fields.add(new LogField("uint", OPERATOR_ID_NAME, "平台Id",
								"", false)); // 每个日志都需要平台id，但是字段里面是不需要记录平台Id的
				Set<String> fieldNames = Sets.newHashSet();
				fieldNames.add(OPERATOR_ID_NAME);
				for (int i = 0; i < entryNodes.getLength(); i++) {
						Node entryNode = entryNodes.item(i);
						if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
								LogField logField = loadLogField(((Element) entryNode), logName);

								if (fieldNames.contains(logField.name)) {
										throw new RuntimeException("日志：" + logName + "中配置了相同的字段名字:" + logField.name);
								}
								fieldNames.add(logField.name);

								fields.add(logField);
						}
				}

				if (fields.isEmpty()) {
						throw new RuntimeException("在日志：" + logName + "中竟然没有配置字段！！！");
				}

				if (!fieldNames.contains(IWORLD_ID)) {
						throw new RuntimeException("在日志：" + logName + "中竟然没有配置服务器Id:iWorldId");
				}

				return new LogTemplate(logName, logDesc, "", eventIdGenerateType, fields,
								dontUseSpecialArg);
		}

		private static LogField loadLogField(Element entryElement, String logName) {
				String fieldName = entryElement.getAttribute("name");
				if (Strings.isNullOrEmpty(fieldName)) {
						throw new RuntimeException("日志：" + logName + "，里面竟然有个字段没有配置名字！！！");
				}

				String postFix = entryElement.getAttribute("post_fix");
				fieldName = postFix != null ? fieldName + postFix : fieldName;

				String fieldType = entryElement.getAttribute("type");
				if (Strings.isNullOrEmpty(fieldType)) {
						throw new RuntimeException("日志：" + logName + "中的字段：" + fieldName + "竟然没有配置类型数据！！！");
				}

				String fieldDesc = entryElement.getAttribute("desc");

				String isBoolStr = entryElement.getAttribute("is_bool");
				boolean isBoolValue = isBoolStr != null && isBoolStr.equals("true");

				return new LogField(fieldType, fieldName, fieldDesc, "", isBoolValue);
		}
}