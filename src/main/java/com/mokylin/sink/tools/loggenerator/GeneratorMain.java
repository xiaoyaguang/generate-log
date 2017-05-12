package com.mokylin.sink.tools.loggenerator;

import com.mokylin.sink.tools.config.ConfigBuilder;
import com.mokylin.sink.tools.loggenerator.config.LogGeneratorConfig;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * Created by yaguang.xiao on 2016/9/14.
 */
public class GeneratorMain {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException, IOException {

        LogGeneratorConfig config = ConfigBuilder
                .buildConfigFromFileName(args[0] + "/log_generator.config",
                        LogGeneratorConfig.class);

        LogServiceGenerator.generate(
                config.toSpecialArgList(),
                config.toReplaceArgList(),
                config.getLogConfigFilePath(),
                config.getLogServiceDir(),
                config.getLogServiceClassFileName(),
                config.getLogServiceTemplateFilePath());
    }

}
