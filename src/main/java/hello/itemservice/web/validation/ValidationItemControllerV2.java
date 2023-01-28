package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bindingResult 및 itemValidator을 이용한 검증
 */
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    //어떤 컨트롤러가 호출되던 WebDataBinder에 itemValidator(검증기)를 넣는다.
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * BindingResult 를 사용한 에러처리
     */
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // *** //
        //BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.

        //검증로직
        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() <1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("price","price","가격은 1,000 ~ 1,000,000까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("quantity","quantity","수량은 최대 9,999까지 허용합니다."));
        }

        // 특정필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if( resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 = "+resultPrice));
            }
        }

        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * BindingResult 를 사용한 에러처리
     * 사용자가 입력한 잘못된 값(rejectedValue)을 보관하여서 그대로 리턴하여, 없어지지 않도록 함.
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증로직
        if(!StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수입니다."));
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,null,null,"상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() <1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("price","price",item.getPrice(),false,null,null,"가격은 1,000 ~ 1,000,000까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("quantity","quantity",item.getQuantity(),false,null,null,"수량은 최대 9,999까지 허용합니다."));
        }

        // 특정필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if( resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",null,null,"가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 = "+resultPrice));
            }
        }

        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * BindingResult 를 사용한 에러처리
     * message처럼 errors.properties를 사용
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={}",bindingResult.getTarget());

        //검증로직
        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,new String[]{"required.item.itemName" , "required.default"},null,null));
            //codes가 배열로 되어있는 이유는 앞선 메세지를 찾을수 없을시 다음 메세지를 찾아서 출력함 -> 디폴트 조차 없으면 에러가 난다.
        }
        if(item.getPrice() == null || item.getPrice() <1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("price","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},null));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("quantity","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999},null));
        }

        // 특정필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if( resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
            }
        }
        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * BindingResult 를 사용한 에러처리
     * bindingResult.addError -> rejectValue 로 변경처리
     */
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //가격부분에 타입오류시 아래와 같이 오류 2개 뜨는 것을 방지하기위하여 타입만 오류일때 한번 걸러준다.
        //숫자를 입력해주세요.
        //가격은 1,000 ~ 1,000,000 까지 허용합니다.
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={}",bindingResult.getTarget());

        //검증로직

        //ValidationUtils 사용시 if문 없이 한줄로 편하게 쓸수있으나, 공백이나 널같은 단순한 체크만 가능
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,"itemName","required");
        // 아래와 같다.
//        if(!StringUtils.hasText(item.getItemName())) {
//            bindingResult.rejectValue("itemName","required");
//        }

        if(item.getPrice() == null || item.getPrice() <1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000},null);
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity","max", new Object[]{9999},null);
        }

        // 특정필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if( resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }
        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * ItemValidatior.java 파일을 생성하여 검증 부분 컨트롤러에서 분리
     */
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if(itemValidator.supports(item.getClass())){
            itemValidator.validate(item,bindingResult);
        }

        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * 스프링 MVC의 인터페이스인 Validator를 상속받은이유- 어노테이션 @Validated를 사용하여
     *  itemValidator.validate(item,bindingResult); 부분 소스 없애기
     *  물론 아래소스도 선언해주어야한다. 33번째줄에 선언하였다.
     *  @InitBinder --> 해당 컨트롤러에 적용하는것이며, 글로벌적용을 위해서는 메인 애플리케이션에 등록해야한다.
     *     public void init(WebDataBinder dataBinder) {
     *         dataBinder.addValidators(itemValidator);
     *     }
     *
     *     @Validated는 검증기를 실행하라 라는 어노테이션이다.
     *     검증기가 여러개일때는 검증기안에 supports메소드가 이용되는데 객체의 이름과 같아야 실행된다.
     */
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

//        if(itemValidator.supports(item.getClass())){
//            itemValidator.validate(item,bindingResult);
//        }

        //검증 실패시 다시 입력폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }




    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

