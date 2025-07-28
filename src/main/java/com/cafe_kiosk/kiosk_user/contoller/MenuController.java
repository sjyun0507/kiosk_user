package com.cafe_kiosk.kiosk_user.contoller;

import com.cafe_kiosk.kiosk_user.dto.CategoryDTO;
import com.cafe_kiosk.kiosk_user.dto.MenuDTO;
import com.cafe_kiosk.kiosk_user.dto.MenuOptionDTO;
import com.cafe_kiosk.kiosk_user.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuController {
    private final MainService mainService;

    @Operation(summary = "카테고리")
    @GetMapping(value = "/")
    public List<CategoryDTO> getCategories() {
        log.info("getCategories");
        List<CategoryDTO> categories = mainService.getAllCategories();
        return categories;
    }

    @Operation(summary = "카테고리 안에 메뉴")
    @GetMapping(value = "/{category_id}")
    public List<MenuDTO> findByCategory(@PathVariable Long category_id) {
        List<MenuDTO> menuDTOS = mainService.getMenusByCategory(category_id);

        return menuDTOS;
    }

    @Operation(summary= "모든 메뉴")
    @GetMapping(value="/all")
    public List<MenuDTO> findAll() {
        log.info("findAll");
        List<MenuDTO> all = mainService.allMenus();
        return all;

    }

    @Operation(summary = "메뉴 상세옵션")
    @GetMapping(value = "/{category_id}/{menu_id}")
    public List<MenuOptionDTO> getMenuOption(@PathVariable Long category_id, @PathVariable Long menu_id) {
        List<MenuOptionDTO> optionDTOS = mainService.getOptions(menu_id);
        return optionDTOS;
    }


}
