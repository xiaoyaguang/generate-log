package com.mokylin.sink.tools.loggenerator;

import com.google.common.collect.Sets;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import com.mokylin.sink.tools.loggenerator.component.LogEntityForTemplate;
import com.mokylin.sink.tools.loggenerator.component.LogTemplate;
import com.mokylin.sink.tools.loggenerator.component.ReplaceArg;
import com.mokylin.sink.tools.loggenerator.component.ReplaceArgManager;
import com.mokylin.sink.tools.loggenerator.component.SpecialArg;
import com.mokylin.sink.tools.loggenerator.component.SpecialArgManager;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 日志服务类生成器
 * @author yaguang.xiao
 *
 */
public class LogServiceGenerator {

		static SpecialArgManager specialArgManager;
		static ReplaceArgManager replaceArgManager;

		public static void generate(
						List<SpecialArg> specialArgConstants, List<ReplaceArg> replaceArgConstants,
						String logConfigFilePath, String logServiceDir, String logServiceClassFileName,
						String logServiceTemplateFilePath)

						throws IOException, SAXException, ParserConfigurationException {

				specialArgManager = new SpecialArgManager(specialArgConstants);

				replaceArgManager = new ReplaceArgManager(replaceArgConstants);

				List<LogTemplate> logTemplates =
								LogTemplateLoader.loadLogTemplateFromXml(logConfigFilePath);

				List<LogEntityForTemplate> logEntityList =
								ConvertToTemplateEntity.convertToTemplateEntities(logTemplates);

				generateLogServiceClass(logEntityList, logServiceDir, logServiceClassFileName,
								logServiceTemplateFilePath);
		}

		private static void generateLogServiceClass(List<LogEntityForTemplate> logEntityList,
						String logServiceDir, String logServiceClassFileName,
						String logServiceTemplateFilePath) {
				VelocityContext _context = new VelocityContext();
				_context.put("list", logEntityList);

				Set<String> importClasses = Sets.newHashSet();
				for (LogEntityForTemplate tmpl : logEntityList) {
						importClasses.addAll(tmpl.getImportClasses());
				}

				_context.put("importClasses", importClasses);
				StringWriter _readWriter = new StringWriter();
				try {
						Velocity.mergeTemplate(logServiceTemplateFilePath, "UTF-8", _context, _readWriter);
				} catch (Exception e) {
						throw new RuntimeException(e);
				}

				File srcDist = new File(logServiceDir);
				if (!srcDist.exists()) {
						if (!srcDist.mkdirs()) {
								throw new RuntimeException("Can't create dir " + srcDist);
						}
				}

				Writer _fileWriter;
				try {
						_fileWriter = new OutputStreamWriter(
										new FileOutputStream(new File(srcDist, logServiceClassFileName)),
										"UTF-8");

						_fileWriter.write(new Formatter().formatSource(_readWriter.toString()));
						_fileWriter.close();
				} catch (IOException e) {
						throw new RuntimeException(e);
				} catch (FormatterException e) {
						e.printStackTrace();
				}
		}

}
