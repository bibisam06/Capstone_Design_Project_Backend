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


}
