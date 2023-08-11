package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.dto.JsonResponse;
import LikeLion.UnderTheCBackend.service.ShoppingService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopping")
@Tag(name = "Shopping API", description = "상품 구매 API")
public class ShoppingController {
    private final ShoppingService shoppingService;

    @Autowired
    ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @GetMapping("/list")
    public JsonResponse getShoppingList(@Parameter(hidden = true) Session session) {
        Buyer buyer = (Buyer) session.getUserProperties().get("user");
        // todo: 장바구니에 등록한 상품 목록을 반환하는 API 구현
        JsonResponse jsonRes = null;
        return jsonRes;
    }

    @GetMapping("/history")
    public JsonResponse getShoppingHistory(@Parameter(hidden = true) Session session) {
        Buyer buyer = (Buyer) session.getUserProperties().get("user");
        // todo: 구매자가 구매한 상품 목록을 반환하는 API 구현
        JsonResponse jsonRes = null;
        return jsonRes;
    }

    @PostMapping("/add")
    public JsonResponse addShoppingList(@Parameter(hidden = true) Session session
        , @Parameter(description = "상품 id") @RequestParam("product_id") Long productId
            , @Parameter(description = "주문 개수") @RequestParam("count") Integer count) {
        Buyer buyer = (Buyer) session.getUserProperties().get("user");
        // todo: 장바구니에 상품을 추가하는 API 구현
        JsonResponse jsonRes = null;
        return jsonRes;
    }

    @DeleteMapping("/delete")
    public JsonResponse deleteShoppingList(@Parameter(hidden = true) Session session
            , @Parameter(description = "상품 id") @RequestParam("product_id") Long productId) {
        Buyer buyer = (Buyer) session.getUserProperties().get("user");
        // todo: 장바구니에 상품을 삭제하는 API 구현
        JsonResponse jsonRes = null;
        return jsonRes;
    }

}
