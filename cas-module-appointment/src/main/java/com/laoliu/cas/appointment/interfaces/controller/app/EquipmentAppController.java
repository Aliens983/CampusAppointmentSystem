package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.EquipmentService;
import com.laoliu.cas.appointment.interfaces.dto.response.EquipmentResponse;
import com.laoliu.cas.common.pojo.PageParam;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备查询接口。
 *
 * @author forever-king
 */
@Tag(name = "设备查询")
@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentAppController {

    private final EquipmentService equipmentService;

    @Operation(summary = "获取设备列表（分页）", description = "分页获取所有设备类服务列表")
    @GetMapping
    public CommonResult<PageResult<EquipmentResponse>> getEquipment(@Valid PageParam pageParam) {
        List<EquipmentResponse> allList = equipmentService.getAvailableEquipment();
        return CommonResult.success(paginateInMemory(allList, pageParam.getPageNo(), pageParam.getPageSize()));
    }

    @Operation(summary = "获取设备分类", description = "获取所有设备分类列表")
    @GetMapping("/categories")
    public CommonResult<List<String>> getCategories() {
        return CommonResult.success(equipmentService.getCategories());
    }

    @Operation(summary = "获取设备详情", description = "根据ID获取单个设备详细信息")
    @GetMapping("/{id}")
    public CommonResult<EquipmentResponse> getEquipmentDetail(@PathVariable Long id) {
        EquipmentResponse equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) {
            return CommonResult.notFound("设备不存在");
        }
        return CommonResult.success(equipment);
    }

    /** 内存分页工具方法，用于 stub / 假数据场景。 */
    private static <T> PageResult<T> paginateInMemory(List<T> allList, int page, int pageSize) {
        int total = allList.size();
        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= total) {
            return PageResult.empty(pageSize, page);
        }
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<T> pageRecords = allList.subList(fromIndex, toIndex);
        long pages = (total + pageSize - 1) / pageSize;
        return PageResult.<T>builder()
                .records(pageRecords)
                .total(total)
                .pageSize(pageSize)
                .current(page)
                .pages(pages)
                .build();
    }
}
