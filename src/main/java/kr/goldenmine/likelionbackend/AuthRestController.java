package kr.goldenmine.likelionbackend;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthRestController {
    private static final String JWT_SECRET = "secretKey";

    private HashMap<String, String> accounts = new HashMap<>();

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(
            final HttpServletRequest req,
            final HttpServletResponse res,
            String id,
            String password) throws Exception {

        if(accounts.containsKey(id) && accounts.get(id).equals(password)) {
            String token = generateJwtToken(id);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed");
        }
    }

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> register(
            final HttpServletRequest req,
            final HttpServletResponse res,
            String id,
            String password) throws Exception {
        if(!accounts.containsKey(id)) {
            accounts.put(id, password);
            return ResponseEntity.ok("succeed");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed");
        }
    }

    @RequestMapping(
            value = "/validate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> validate(
            final HttpServletRequest req,
            final HttpServletResponse res) {
        String jwt = getJwtFromRequest(req);

        if(StringUtils.isNotEmpty(jwt)) {
//            System.out.println("jwt: " + jwt);
            Claims claims = parseJwtToken(jwt);
//            System.out.println(claims);
//            System.out.println(claims.getId());
//            System.out.println(claims.getSubject());
//            System.out.println(claims.getExpiration());

            String id = claims.getId();

            if(accounts.containsKey(id) && validateToken(jwt)) {
                return ResponseEntity.ok(id);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public String generateJwtToken(String id) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .setIssuer("fresh") // (2)
                .setIssuedAt(now) // (3)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // (4)
//                .claim("id", id) // (5)
//                .setSubject(id)
                .setId(id)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // (6)
                .compact();
    }

    public Claims parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET) // (3)
                .parseClaimsJws(token) // (4)
                .getBody();
    }

    // Jwt 토큰에서 아이디 추출
    public static String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Jwt 토큰 유효성 검사
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
