import com.itheima.domain.Items;
import com.itheima.service.impl.ItemsServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ItemsServiceTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext cpa = new ClassPathXmlApplicationContext("aplicationContext.xml");
        ItemsServiceImpl itemsService = cpa.getBean( ItemsServiceImpl.class);
        Items list = itemsService.findAll(1);
        System.out.println(list);
    }
}
