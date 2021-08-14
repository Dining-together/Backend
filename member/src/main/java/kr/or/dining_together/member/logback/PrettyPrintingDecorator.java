package kr.or.dining_together.member.logback;

import net.logstash.logback.decorate.JsonGeneratorDecorator;

import com.fasterxml.jackson.core.JsonGenerator;

public class PrettyPrintingDecorator implements JsonGeneratorDecorator {

	@Override
	public JsonGenerator decorate(JsonGenerator generator) {

		return generator.useDefaultPrettyPrinter();
	}
}
