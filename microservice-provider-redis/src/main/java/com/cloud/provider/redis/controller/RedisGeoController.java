package com.cloud.provider.redis.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.CommConstants;
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;

import io.swagger.annotations.Api;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/**
 * Redis String（字符串） RedisGeoController
 * @author wei.yong
 * @date 2017-09-14
 */
@Api(tags = "地理位置")
@RestController
@RequestMapping("/redis/geo")
public class RedisGeoController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO geo(地理位置) 命令工具方法
	/************************** jredis geo(地理位置) 命令工具方法 ****************************/
	/**
	 * 将给定的空间元素（纬度、经度、名字）添加到指定的键里面
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/geoadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geoadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(longitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "longitude为空");
		} else if(latitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "latitude为空");
		} else if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.geoadd(key, longitude, latitude, member);
			logger.info("===step2:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoadd)-添加到指定的键里面--返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoadd)-添加到指定的键里面--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/**
	 * 将给定的空间元素（纬度、经度、名字）添加到指定的键里面
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/map/geoadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geoaddMap(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoaddMap)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Map<String, GeoCoordinate> memberCoordinateMap = req.getMemberCoordinateMap();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(memberCoordinateMap == null || memberCoordinateMap.size() == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "memberCoordinateMap为空");
		}

		long len = 0l;
		try {
			len = redisService.geoadd(key, memberCoordinateMap);
			logger.info("===step2:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoaddMap)-添加到指定的键里面--返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoaddMap)-添加到指定的键里面--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(RedisGeoController-geoaddMap)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回所有给定位置元素的位置（经度和纬度）
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/geopos",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geopos(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回所有给定位置元素的位置（经度和纬度）】(RedisGeoController-geopos)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		List<GeoCoordinate> result = null;
		try {
			result = redisService.geopos(key, member);
			logger.info("===step2:【返回所有给定位置元素的位置（经度和纬度）】(RedisGeoController-geopos)-给定位置元素的位置--返回信息, result:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回所有给定位置元素的位置（经度和纬度）】(RedisGeoController-geopos)-给定位置元素的位置--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【返回所有给定位置元素的位置（经度和纬度）】(RedisGeoController-geopos)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回两个给定位置之间的距离
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/geodist",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geodist(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回两个给定位置之间的距离】(RedisGeoController-geodist)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member1 = req.getMember1();
		String member2 = req.getMember2();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member1)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member1为空");
		} else if(StringUtils.isBlank(member2)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member2为空");
		}

		double len = 0d;
		try {
			len = redisService.geodist(key, member1, member2);
			logger.info("===step2:【返回两个给定位置之间的距离】(RedisGeoController-geodist)-返回两个给定位置之间的距离--返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回两个给定位置之间的距离】(RedisGeoController-geodist)-返回两个给定位置之间的距离--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【返回两个给定位置之间的距离】(RedisGeoController-geodist)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据单位返回两个给定位置之间的距离
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/unit/geodist",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geodistUnit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据单位返回两个给定位置之间的距离】(RedisGeoController-geodistUnit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member1 = req.getMember1();
		String member2 = req.getMember2();
		String unit = req.getGeoUnit();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member1)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member1为空");
		} else if(StringUtils.isBlank(member2)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member2为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "unit为空");
		}

		double len = 0d;
		try {
			if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
				len = redisService.geodist(key, member1, member2, GeoUnit.M);
			} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
				len = redisService.geodist(key, member1, member2, GeoUnit.KM);
			} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
				len = redisService.geodist(key, member1, member2, GeoUnit.MI);
			} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
				len = redisService.geodist(key, member1, member2, GeoUnit.FT);
			}
			logger.info("===step2:【根据单位返回两个给定位置之间的距离】(RedisGeoController-geodistUnit)-返回两个给定位置之间的距离-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【根据单位返回两个给定位置之间的距离】(RedisGeoController-geodistUnit)-返回两个给定位置之间的距离-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【根据单位返回两个给定位置之间的距离】(RedisGeoController-geodistUnit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定的经纬度为中心，返回键包含的位置元素当中，与中心的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/georadius",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse georadius(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(longitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "longitude为空");
		} else if(latitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "latitude为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "unit为空");
		} else if(radius == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "radius为空");
		}

		List<GeoRadiusResponse> result = null;
		try {
			if(GeoUnit.M.name().equals(unit)) {
				result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M);
			} else if(GeoUnit.KM.name().equals(unit)) {
				result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM);
			} else if(GeoUnit.MI.name().equals(unit)) {
				result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI);
			} else if(GeoUnit.FT.name().equals(unit)) {
				result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT);
			}
			logger.info("===step2:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/param/georadius",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse georadiusParam(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		String geoRadiusParam = req.getGeoRadiusParam();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(longitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "longitude为空");
		} else if(latitude == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "latitude为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "unit为空");
		} else if(radius == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "radius为空");
		} else if(StringUtils.isBlank(geoRadiusParam)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "geoRadiusParam为空");
		} else if(count == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "count为空");
		}

		GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();

		List<GeoRadiusResponse> result = null;
		try {
			if(StringUtils.equalsIgnoreCase(geoRadiusParam, "withcoord")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M, param.withCoord());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM, param.withCoord());
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI, param.withCoord());
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT, param.withCoord());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "withdist")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M, param.withCoord());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM, param.withDist());
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI, param.withDist());
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT, param.withDist());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "asc")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M, param.sortAscending());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM, param.sortAscending());
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI, param.sortAscending());
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT, param.sortAscending());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "desc")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M, param.sortDescending());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM, param.sortDescending());
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI, param.sortDescending());
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT, param.sortDescending());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "count")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.M, param.count(count.intValue()));
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.KM, param.count(count.intValue()));
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.MI, param.count(count.intValue()));
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadius(key, longitude, latitude, radius, GeoUnit.FT, param.count(count.intValue()));
				}
			}
			logger.info("===step2:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadius)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定中心点的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/georadiusByMember",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse georadiusByMember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		} else if(radius == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "radius为空");
		}

		List<GeoRadiusResponse> result = null;
		try {
			if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
				result = redisService.georadiusByMember(key, member, radius, GeoUnit.M);
			} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
				result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM);
			} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
				result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI);
			} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
				result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT);
			}
			logger.info("===step2:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定中心点的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/param/georadiusByMember",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse georadiusByMemberParam(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		String geoRadiusParam = req.getGeoRadiusParam();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		} else if(radius == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "radius为空");
		} else if(StringUtils.isBlank(geoRadiusParam)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "geoRadiusParam为空");
		} else if(count == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "count为空");
		}

		GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();

		List<GeoRadiusResponse> result = null;
		try {
			if(StringUtils.equalsIgnoreCase(geoRadiusParam, "withcoord")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.M, param.withCoord());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM, param.withCoord());
				} else if(GeoUnit.MI.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI, param.withCoord());
				} else if(GeoUnit.FT.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT, param.withCoord());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "withdist")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.M, param.withDist());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM, param.withDist());
				} else if(GeoUnit.MI.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI, param.withDist());
				} else if(GeoUnit.FT.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT, param.withDist());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "asc")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.M, param.sortAscending());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM, param.sortAscending());
				} else if(GeoUnit.MI.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI, param.sortAscending());
				} else if(GeoUnit.FT.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT, param.sortAscending());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "desc")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.M, param.sortDescending());
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM, param.sortDescending());
				} else if(GeoUnit.MI.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI, param.sortDescending());
				} else if(GeoUnit.FT.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT, param.sortDescending());
				}
			} else if(StringUtils.equalsIgnoreCase(geoRadiusParam, "count")) {
				if(GeoUnit.M.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.M,param.count(count.intValue()));
				} else if(GeoUnit.KM.name().equalsIgnoreCase(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.KM, param.count(count.intValue()));
				} else if(GeoUnit.MI.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.MI, param.count(count.intValue()));
				} else if(GeoUnit.FT.name().equals(unit)) {
					result = redisService.georadiusByMember(key, member, radius, GeoUnit.FT, param.count(count.intValue()));
				}
			}
			logger.info("===step2:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【以给定中心点的距离不超过给定最大距离的所有位置元素】(RedisGeoController-georadiusByMember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回一个位置元素的Geohash表
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/geohash",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geohash(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个位置元素的Geohash表】(RedisGeoController-geohash)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		List<String> result = null;
		try {
			result = redisService.geohash(key, member);
			logger.info("===step2:【一个位置元素的Geohash表】(RedisGeoController-geohash)-一个位置元素的Geohash-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【一个位置元素的Geohash表】(RedisGeoController-geohash)-一个位置元素的Geohash-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【一个位置元素的Geohash表】(RedisGeoController-geohash)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回多个位置元素的Geohash表
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/geohash",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse geohashArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个位置元素的Geohash表】(RedisGeoController-geohashArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(members == null || members.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "members为空");
		}

		List<String> result = null;
		try {
			result = redisService.geohash(key, members);
			logger.info("===step2:【多个位置元素的Geohash表】(RedisGeoController-geohashArray)-多个位置元素的Geohash-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个位置元素的Geohash表】(RedisGeoController-geohashArray)-多个位置元素的Geohash-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【多个位置元素的Geohash表】(RedisGeoController-geohashArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis geo(地理位置) 命令工具方法 ****************************/

}