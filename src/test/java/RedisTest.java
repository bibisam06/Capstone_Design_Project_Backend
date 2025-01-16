import com.bibisam.dobee.Entity.AuthenticationToken;
import com.bibisam.dobee.Repository.RedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    private final RedisRepository repository;

    public RedisTest(RedisRepository repository) {
        this.repository = repository;
    }

    @Test
    @DisplayName("redis - 인증토큰 생성, 만료 테스트")
    public void test1() {
        //given
        AuthenticationToken token1 = new AuthenticationToken("1", 10000, 10L);
        //when
        repository.save(token1);
        //then
    }
}
