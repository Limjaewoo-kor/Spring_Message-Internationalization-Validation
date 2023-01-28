package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    /**
     * 요청 실패 [bad request] - 타입오류로 ItemSaveForm형식과 달라서 controller 호출 조차 되지않음.
     *   ->   HTTP 메시지 컨버터 자체가 실패한것임
     * 검증오류 - 필드의 수치가 틀려서 bean Validated에 걸려서 오류 발생
     * 성공 - 성공
     */
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API Controller 호출");

        if (bindingResult.hasErrors()){
            log.info("error={}",bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
