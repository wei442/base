package com.cloud.provider.redis.rest.request;

import java.util.Map;

import com.cloud.provider.redis.boot.BootRestRequest;

import redis.clients.jedis.GeoCoordinate;

public class RedisRequest extends BootRestRequest {

	private static final long serialVersionUID = 1L;

	private String key;

	private String value;

	private Integer seconds;

	private Long unixTime;

	private Long milliseconds;

	private Long millisecondsTimestamp;

	private Long startOffset;

	private Long endOffset;

	private Long offset;

	private String pattern;

	private String host;
	private Integer port;
	private Integer destinationDb;

	private Integer dbIndex;

	private Integer ttl;
	private byte[] serializedValue;

	private String sortingParameters;

	private String oldkey;
	private String newkey;

	private String field;

	private Integer timeout;

	private String source;
	private String destination;

	private Long index;

	private Long from;
	private Long to;
	private Long count;

	private Long start;
	private Long end;

	private String srckey;
	private String dstkey;

	private String[] srckeys;
	private String operation;

	private String argument;
	private String[] arguments;

	private Double score;
	private String member;

	private Double min;
	private Double max;

	private String minStr;
	private String maxStr;

	private String cursor;
	private String password;
	private String string;

	private String channel;

	private String message;

	private String[] keys;

	private String[] values;

	private String[] keysvalues;

	private String[] fields;

	private String[] members;

	private Map<String, String> hash;

	private String element;

	private String[] elements;

	private String sourcekey;
	private String[] sourcekeys;

	private String where;
	private String pivot;

	private Long longitude;
	private Long latitude;
	private Double radius;

	private Map<String, GeoCoordinate> memberCoordinateMap;

	private String member1;
	private String member2;
	private String geoUnit;

	private String geoRadiusParam;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSeconds() {
		return this.seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public Long getUnixTime() {
		return this.unixTime;
	}

	public void setUnixTime(Long unixTime) {
		this.unixTime = unixTime;
	}

	public Long getMilliseconds() {
		return this.milliseconds;
	}

	public void setMilliseconds(Long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public Long getMillisecondsTimestamp() {
		return this.millisecondsTimestamp;
	}

	public void setMillisecondsTimestamp(Long millisecondsTimestamp) {
		this.millisecondsTimestamp = millisecondsTimestamp;
	}

	public Long getStartOffset() {
		return this.startOffset;
	}

	public void setStartOffset(Long startOffset) {
		this.startOffset = startOffset;
	}

	public Long getEndOffset() {
		return this.endOffset;
	}

	public void setEndOffset(Long endOffset) {
		this.endOffset = endOffset;
	}

	public Long getOffset() {
		return this.offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getDestinationDb() {
		return this.destinationDb;
	}

	public void setDestinationDb(Integer destinationDb) {
		this.destinationDb = destinationDb;
	}

	public Integer getDbIndex() {
		return this.dbIndex;
	}

	public void setDbIndex(Integer dbIndex) {
		this.dbIndex = dbIndex;
	}

	public Integer getTtl() {
		return this.ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	public byte[] getSerializedValue() {
		return this.serializedValue;
	}

	public void setSerializedValue(byte[] serializedValue) {
		this.serializedValue = serializedValue;
	}

	public String getSortingParameters() {
		return this.sortingParameters;
	}

	public void setSortingParameters(String sortingParameters) {
		this.sortingParameters = sortingParameters;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getOldkey() {
		return this.oldkey;
	}

	public void setOldkey(String oldkey) {
		this.oldkey = oldkey;
	}

	public String getNewkey() {
		return this.newkey;
	}

	public void setNewkey(String newkey) {
		this.newkey = newkey;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Integer getTimeout() {
		return this.timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getIndex() {
		return this.index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getFrom() {
		return this.from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return this.to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getStart() {
		return this.start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return this.end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public String getSrckey() {
		return this.srckey;
	}

	public void setSrckey(String srckey) {
		this.srckey = srckey;
	}

	public String getDstkey() {
		return this.dstkey;
	}

	public void setDstkey(String dstkey) {
		this.dstkey = dstkey;
	}

	public String[] getSrckeys() {
		return this.srckeys;
	}

	public void setSrckeys(String[] srckeys) {
		this.srckeys = srckeys;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getArgument() {
		return this.argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public String[] getArguments() {
		return this.arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getMember() {
		return this.member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public Double getMin() {
		return this.min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return this.max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public String getMinStr() {
		return this.minStr;
	}

	public void setMinStr(String minStr) {
		this.minStr = minStr;
	}

	public String getMaxStr() {
		return this.maxStr;
	}

	public void setMaxStr(String maxStr) {
		this.maxStr = maxStr;
	}

	public String getCursor() {
		return this.cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getString() {
		return this.string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String getChannel() {
		return this.channel;
	}

	@Override
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getKeys() {
		return this.keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String[] getValues() {
		return this.values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getKeysvalues() {
		return this.keysvalues;
	}

	public void setKeysvalues(String[] keysvalues) {
		this.keysvalues = keysvalues;
	}

	public String[] getFields() {
		return this.fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getMembers() {
		return this.members;
	}

	public void setMembers(String[] members) {
		this.members = members;
	}

	public Map<String, String> getHash() {
		return this.hash;
	}

	public void setHash(Map<String, String> hash) {
		this.hash = hash;
	}

	public String getElement() {
		return this.element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String[] getElements() {
		return this.elements;
	}

	public void setElements(String[] elements) {
		this.elements = elements;
	}

	public String getSourcekey() {
		return this.sourcekey;
	}

	public void setSourcekey(String sourcekey) {
		this.sourcekey = sourcekey;
	}

	public String[] getSourcekeys() {
		return this.sourcekeys;
	}

	public void setSourcekeys(String[] sourcekeys) {
		this.sourcekeys = sourcekeys;
	}

	public String getWhere() {
		return this.where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getPivot() {
		return this.pivot;
	}

	public void setPivot(String pivot) {
		this.pivot = pivot;
	}

	public Long getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	public Long getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Double getRadius() {
		return this.radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Map<String, GeoCoordinate> getMemberCoordinateMap() {
		return this.memberCoordinateMap;
	}

	public void setMemberCoordinateMap(Map<String, GeoCoordinate> memberCoordinateMap) {
		this.memberCoordinateMap = memberCoordinateMap;
	}

	public String getMember1() {
		return this.member1;
	}

	public void setMember1(String member1) {
		this.member1 = member1;
	}

	public String getMember2() {
		return this.member2;
	}

	public void setMember2(String member2) {
		this.member2 = member2;
	}

	public String getGeoUnit() {
		return this.geoUnit;
	}

	public void setGeoUnit(String geoUnit) {
		this.geoUnit = geoUnit;
	}

	public String getGeoRadiusParam() {
		return this.geoRadiusParam;
	}

	public void setGeoRadiusParam(String geoRadiusParam) {
		this.geoRadiusParam = geoRadiusParam;
	}




}