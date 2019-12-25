package top.rizon.springbestpractice.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rizon.springbestpractice.common.model.request.PageParam;
import top.rizon.springbestpractice.common.model.response.PageResponse;
import top.rizon.springbestpractice.common.model.response.Response;
import top.rizon.springbestpractice.dao.helper.UserHelper;
import top.rizon.springbestpractice.dao.po.User;
import top.rizon.springbestpractice.web.model.WebObjMapper;
import top.rizon.springbestpractice.web.model.param.UserQueryParam;
import top.rizon.springbestpractice.web.model.vo.UserVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rizon
 * @date 2019/12/25
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserHelper userHelper;
    private final WebObjMapper webObjMapper;

    @GetMapping("list")
    public Response<List<UserVo>> list(UserQueryParam param) {
        log.info("request list user,param:{}", param);
        PageParam pageParam = param.getPageParam();
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getEmailLike()), User::getEmail, param.getEmailLike());

        if (pageParam != null) {
            IPage<User> result = userHelper.page(new Page<>(pageParam.getPage(), pageParam.getPageSize()), queryWrapper);
            return PageResponse.success(
                    result.getRecords().stream().map(webObjMapper::toVo).collect(Collectors.toList()),
                    pageParam.setTotalPage(result.getTotal())
                            .setTotalPage(result.getPages()));
        } else {
            return Response.success(userHelper.list(queryWrapper).stream()
                    .map(webObjMapper::toVo).collect(Collectors.toList()));
        }
    }
}
