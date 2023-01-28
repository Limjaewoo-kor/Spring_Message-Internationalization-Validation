package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;
    @NotBlank(message = "공백일 수 없습니다.")
    private String itemName;
    @NotNull(message = "비어있을 수 없습니다.")
    @Range(min = 1000, max = 1000000)
    private Integer price;
    @NotNull(message = "비어있을 수 없습니다.")
    private Integer quantity;

}
