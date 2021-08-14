package kr.or.dining_together.auction.logback;

import net.logstash.logback.decorate.JsonFactoryDecorator;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class ISO8601DateDecorator implements JsonFactoryDecorator {

	@Override
	public MappingJsonFactory decorate(MappingJsonFactory factory) {

		ObjectMapper codec = factory.getCodec();
		codec.setDateFormat(new StdDateFormat());

		return factory;
	}
}
