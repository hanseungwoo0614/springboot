package com.example.ksl.wsd2025.api.v1;

import com.example.ksl.wsd2025.api.dto.ItemDto;
import com.example.ksl.wsd2025.api.request.ItemCreateRequest;
import com.example.ksl.wsd2025.api.request.ItemUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ksl.wsd2025.common.ApiResponse;

import java.util.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    // 메모리용 임시 DB (예제용)
    private final Map<Long, ItemDto> store = new HashMap<>();
    private long sequence = 1L;

    // ===================== GET =====================

    // 1) GET: 전체 조회  200
    //    ?simulate=500 / ?simulate=503 을 붙이면 테스트용 서버 오류
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDto>>> getItems(@RequestParam(required = false) String simulate) {
        if ("500".equals(simulate)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("서버 내부 오류가 발생했습니다."));
        }
        if ("503".equals(simulate)) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("서버 점검 중입니다. 잠시 후 다시 시도해주세요."));
        }
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(store.values())));
    }

    // 2) GET: 단건 조회 200 / 404
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> getItem(@PathVariable Long id) {
        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("id=" + id + " 상품을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    // ===================== POST =====================

    // 3) POST: 상품 1개 등록  201 / 400
    @PostMapping
    public ResponseEntity<ApiResponse<ItemDto>> createItem(@RequestBody ItemCreateRequest request) {
        if (request.getName() == null || request.getName().isBlank()
                || request.getPrice() == null || request.getPrice() < 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("name은 비어 있을 수 없고, price는 0 이상이어야 합니다."));
        }

        ItemDto item = new ItemDto();
        item.setId(sequence++);
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        store.put(item.getId(), item);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(item));
    }

    // 4) POST: 상품 여러 개 등록  201 / 400
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<ItemDto>>> createItems(@RequestBody List<ItemCreateRequest> requests) {
        if (requests.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("등록할 상품 목록이 비어 있습니다."));
        }

        List<ItemDto> created = new ArrayList<>();
        for (ItemCreateRequest request : requests) {
            ItemDto item = new ItemDto();
            item.setId(sequence++);
            item.setName(request.getName());
            item.setPrice(request.getPrice());
            store.put(item.getId(), item);
            created.add(item);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    // ===================== PUT =====================

    // 5) PUT: 상품 수정  200 / 404
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("id=" + id + " 상품을 찾을 수 없습니다."));
        }

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    // 6) PUT: 가격만 수정 200 / 400 / 404
    @PutMapping("/{id}/price")
    public ResponseEntity<ApiResponse<ItemDto>> updatePrice(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        if (request.getPrice() == null || request.getPrice() < 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("price는 0 이상이어야 합니다."));
        }

        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("id=" + id + " 상품을 찾을 수 없습니다."));
        }
        item.setPrice(request.getPrice());
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    // ===================== DELETE =====================

    // 7) DELETE: 단건 삭제  200 / 404
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> deleteItem(@PathVariable Long id) {
        ItemDto removed = store.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("id=" + id + " 상품을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success(removed, "삭제되었습니다."));
    }

    // 8) DELETE: 전체 삭제  200
    @DeleteMapping
    public ResponseEntity<ApiResponse<Map<String, Integer>>> deleteAll() {
        int count = store.size();
        store.clear();
        return ResponseEntity.ok(ApiResponse.success(Map.of("deletedCount", count), "전체 삭제되었습니다."));
    }
}