package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
//@ScriptAssert 기능이 너무 약하여 권장하지않고, 자바 코드로 하기를 권장한다.
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000" ,message = "가격 * 수량이 10000원이 넘어야합니다.")
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open;   //판매여부

    private List<String> regions;   //등록지역

    private ItemType itemType;  //상품종류

    private String deliveryCode;    //배송방식


    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
