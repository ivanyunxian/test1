package com.supermap.intelligent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.crypto.hash.Md5Hash;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * token工具
 * @author heng
 *
 */
public class JwtUtils {
	//编译期常量，服务器非当天重启时，需要重新申请
	private final static String  SECRETKEY = new Md5Hash("2019@,gxbdc" + new SimpleDateFormat("yyyyMMdd").format(new Date())).toString();
//	//固定密码
//	private final static String  SECRETKEY = "ZXM0b0VoODJpMTB4amRVMjNrZGN2QCUyNDYzY2Q=";
	public final static String JWT_TOKEN_OK = "00";
	public final static String JWT_TOKEN_EXPIRED = "42";
	public final static String JWT_TOKEN_FAIL = "43";
	
	/**
	 * 创建token
	 * @param id
	 * @param subject
	 * @param time 创建时间
	 * @param timeout 有效时间 单位ms
	 * @return
	 */
	public static String createToken(String id,String subject,Date time,long timeout){
		JwtBuilder jwtBuilder = Jwts.builder()
				.setId(id)
				.setSubject(subject)
				.setIssuedAt(time)//登录时间-也就是签发时间（签发给你token的时间）
				.setExpiration(new Date(time.getTime() + timeout))//设置过期时间-一分钟
				.signWith(SignatureAlgorithm.HS256,SECRETKEY);
		return jwtBuilder.compact();
	}
	
    /**
     * 验证JWT
     * @param jwtStr
     * @return 00:JWT_TOKEN_OK,42:JWT_TOKEN_EXPIRED,43:JWT_TOKEN_FAIL
     */
    public static String verifyToken(String token) {
        try {
        	Claims  claims = parseJWT(token);
            return JWT_TOKEN_OK;
        } catch (ExpiredJwtException e) {
        	return JWT_TOKEN_EXPIRED;
        } catch (SignatureException e) {
        	return JWT_TOKEN_FAIL;
        } catch (Exception e) {
        	return JWT_TOKEN_FAIL;
        }
    }
    
    /**
     * 
     * 解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String token) throws Exception {
        return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
    }
    
    public static String getSubject(String token) throws Exception{
    	Claims claims = parseJWT(token);
    	return claims.getSubject();
    }

}
