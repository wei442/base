package com.ochain.provider.redis.controllers;

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
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/**
 * Redis String（字符串） BootRedisGeoController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/geo")
public class BootRedisGeoController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO geo(地理位置) 命令工具方法
	/************************** jredis geo(地理位置) 命令工具方法 ****************************/
	/**
	 * 将给定的空间元素（纬度、经度、名字）添加到指定的键里面
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/geoadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geoadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(longitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "longitude为空");
		} else if(latitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "latitude为空");
		} else if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.geoadd(key, longitude, latitude, member);
			logger.info("===step2:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoadd)-添加到指定的键里面--返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoadd)-添加到指定的键里面--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/**
	 * 将给定的空间元素（纬度、经度、名字）添加到指定的键里面
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/map/geoadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geoaddMap(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoaddMap)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Map<String, GeoCoordinate> memberCoordinateMap = req.getMemberCoordinateMap();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(memberCoordinateMap == null || memberCoordinateMap.size() == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "memberCoordinateMap为空");
		}

		long len = 0l;
		try {
			len = redisService.geoadd(key, memberCoordinateMap);
			logger.info("===step2:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoaddMap)-添加到指定的键里面--返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoaddMap)-添加到指定的键里面--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将给定的空间元素（纬度、经度、名字）添加到指定的键里面】(BootRedisGeoController-geoaddMap)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回所有给定位置元素的位置（经度和纬度）
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/geopos",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geopos(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回所有给定位置元素的位置（经度和纬度）】(BootRedisGeoController-geopos)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		List<GeoCoordinate> result = null;
		try {
			result = redisService.geopos(key, member);
			logger.info("===step2:【返回所有给定位置元素的位置（经度和纬度）】(BootRedisGeoController-geopos)-给定位置元素的位置--返回信息, result:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回所有给定位置元素的位置（经度和纬度）】(BootRedisGeoController-geopos)-给定位置元素的位置--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回所有给定位置元素的位置（经度和纬度）】(BootRedisGeoController-geopos)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回两个给定位置之间的距离
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/geodist",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geodist(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回两个给定位置之间的距离】(BootRedisGeoController-geodist)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member1 = req.getMember1();
		String member2 = req.getMember2();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member1)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member1为空");
		} else if(StringUtils.isBlank(member2)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member2为空");
		}

		double len = 0d;
		try {
			len = redisService.geodist(key, member1, member2);
			logger.info("===step2:【返回两个给定位置之间的距离】(BootRedisGeoController-geodist)-返回两个给定位置之间的距离--返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回两个给定位置之间的距离】(BootRedisGeoController-geodist)-返回两个给定位置之间的距离--事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回两个给定位置之间的距离】(BootRedisGeoController-geodist)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据单位返回两个给定位置之间的距离
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/unit/geodist",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geodistUnit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据单位返回两个给定位置之间的距离】(BootRedisGeoController-geodistUnit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member1 = req.getMember1();
		String member2 = req.getMember2();
		String unit = req.getGeoUnit();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member1)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member1为空");
		} else if(StringUtils.isBlank(member2)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member2为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "unit为空");
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
			logger.info("===step2:【根据单位返回两个给定位置之间的距离】(BootRedisGeoController-geodistUnit)-返回两个给定位置之间的距离-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【根据单位返回两个给定位置之间的距离】(BootRedisGeoController-geodistUnit)-返回两个给定位置之间的距离-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【根据单位返回两个给定位置之间的距离】(BootRedisGeoController-geodistUnit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定的经纬度为中心，返回键包含的位置元素当中，与中心的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/georadius",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse georadius(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(longitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "longitude为空");
		} else if(latitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "latitude为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "unit为空");
		} else if(radius == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "radius为空");
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
			logger.info("===step2:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/param/georadius",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse georadiusParam(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long longitude = req.getLongitude();
		Long latitude = req.getLatitude();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		String geoRadiusParam = req.getGeoRadiusParam();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(longitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "longitude为空");
		} else if(latitude == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "latitude为空");
		} else if(StringUtils.isBlank(unit)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "unit为空");
		} else if(radius == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "radius为空");
		} else if(StringUtils.isBlank(geoRadiusParam)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "geoRadiusParam为空");
		} else if(count == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "count为空");
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
			logger.info("===step2:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【给定的经纬度为中心的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadius)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定中心点的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/georadiusByMember",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse georadiusByMember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		} else if(radius == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "radius为空");
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
			logger.info("===step2:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以给定中心点的距离不超过给定最大距离的所有位置元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/param/georadiusByMember",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse georadiusByMemberParam(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		String unit = req.getGeoUnit();
		Double radius = req.getRadius();
		String geoRadiusParam = req.getGeoRadiusParam();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		} else if(radius == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "radius为空");
		} else if(StringUtils.isBlank(geoRadiusParam)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "geoRadiusParam为空");
		} else if(count == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "count为空");
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
			logger.info("===step2:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-给定中心点的距离不超过给定最大距离的所有位置元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【以给定中心点的距离不超过给定最大距离的所有位置元素】(BootRedisGeoController-georadiusByMember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回一个位置元素的Geohash表
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/geohash",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geohash(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个位置元素的Geohash表】(BootRedisGeoController-geohash)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		List<String> result = null;
		try {
			result = redisService.geohash(key, member);
			logger.info("===step2:【一个位置元素的Geohash表】(BootRedisGeoController-geohash)-一个位置元素的Geohash-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【一个位置元素的Geohash表】(BootRedisGeoController-geohash)-一个位置元素的Geohash-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【一个位置元素的Geohash表】(BootRedisGeoController-geohash)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回多个位置元素的Geohash表
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/geohash",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse geohashArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个位置元素的Geohash表】(BootRedisGeoController-geohashArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(members == null || members.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "members为空");
		}

		List<String> result = null;
		try {
			result = redisService.geohash(key, members);
			logger.info("===step2:【多个位置元素的Geohash表】(BootRedisGeoController-geohashArray)-多个位置元素的Geohash-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【多个位置元素的Geohash表】(BootRedisGeoController-geohashArray)-多个位置元素的Geohash-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【多个位置元素的Geohash表】(BootRedisGeoController-geohashArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis geo(地理位置) 命令工具方法 ****************************/

}