package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.entity.ShoppingHistory;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.dto.JsonResponse;
import LikeLion.UnderTheCBackend.entity.ShoppingList;
import LikeLion.UnderTheCBackend.service.ProductService;
import LikeLion.UnderTheCBackend.service.ShoppingService;
import LikeLion.UnderTheCBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping")
@Tag(name = "Shopping API", description = "구매 API")
public class ShoppingController {
    private final ShoppingService shoppingService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    ShoppingController(ShoppingService shoppingService, ProductService productService, UserService userService) {
        this.shoppingService = shoppingService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/list")
    @Operation(summary = "장바구니 조회", description = "세션에 저장된 User의 Id로 장바구니 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 완료")
    })
    public JsonResponse getShoppingList(@Parameter(hidden = true)HttpServletRequest request) {
        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        User user = userService.findUser((Long) session.getAttribute("user"));

        List<ShoppingList> list = shoppingService.findListByUserId(user.getId());
        return new JsonResponse("200", "success", list);
    }

    @PostMapping("/add")
    @Operation(summary = "장바구니에 상풍 추가", description = "세션에 저장된 User의 Id로 장바구니에 추가", responses = {
            @ApiResponse(responseCode = "200", description = "장바구니 추가 완료")
    })
    public JsonResponse addShoppingList(@Parameter(hidden = true) HttpServletRequest request
        , @Parameter(description = "상품 id") @RequestParam("product_id") Long productId
            , @Parameter(description = "주문 개수") @RequestParam("count") Integer count) {
        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        User user = userService.findUser((Long) session.getAttribute("user"));

        Product product = productService.getProduct(productId);
        List<ShoppingList> shoppingLists = shoppingService.addShoppingList(user, product, count);
        JsonResponse jsonRes = new JsonResponse("200", "success", shoppingLists);
        return jsonRes;
    }

    @DeleteMapping("/delete")
    public JsonResponse deleteShoppingList(@Parameter(hidden = true) HttpServletRequest request
            , @Parameter(description = "상품 id") @RequestParam("product_id") Long productId
            , @Parameter(description = "주문 개수") @RequestParam("count") Integer count) {
        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        User user = userService.findUser((Long) session.getAttribute("user"));

        ShoppingList list = shoppingService.deleteByUserIdAndProductId(user.getId(), productId, count);
        return new JsonResponse("200", "success", list);
    }

/**
 * 여기서부터 구매 기록 API
 * */

    @GetMapping("/history")
    @Operation(summary = "구매기록 조회", description = "세션에 저장된 User의 Id로 구매기록 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 완료")
    })
    public JsonResponse getShoppingHistory(@Parameter(hidden = true) HttpServletRequest request) {
        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        User user = userService.findUser((Long) session.getAttribute("user"));

        List<ShoppingHistory> list = shoppingService.findHistoryByUserId(user.getId());
        return new JsonResponse("200", "success", list);
    }

}
