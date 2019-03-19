package com.dingding.seckill.controller;

import com.dingding.seckill.dto.Exposer;
import com.dingding.seckill.dto.SeckillExecution;
import com.dingding.seckill.dto.SeckillResult;
import com.dingding.seckill.entity.Seckill;
import com.dingding.seckill.enums.SeckillStateEnum;
import com.dingding.seckill.exception.RepeatKillException;
import com.dingding.seckill.exception.SeckillCloseException;
import com.dingding.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用接口
 *
 * @author liudingding
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(Model m) {
        return "hello";
    }



    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;

    /**
     * 列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        //获取列表页
        model.addAttribute("list", list);
        return "list";
    }

    /**
     * 列表详情页
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "seckill_detail";
    }

    /**
     * 暴露秒杀地址
     *
     * @param seckillId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execute", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone) {

        SeckillResult<SeckillExecution> result = null;
        SeckillExecution seckillExecution = null;
        if (userPhone == null) {
            return new SeckillResult<>(false, "未注册");
        } else {
            try {
                seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                result =  new SeckillResult<>(true, seckillExecution);
            } catch (RepeatKillException e) {
                seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
                result =  new SeckillResult<>(true, seckillExecution);
            } catch (SeckillCloseException e) {
                seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
                result =  new SeckillResult<>(true, seckillExecution);
            } catch (Exception e) {
                seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
                result =  new SeckillResult<>(true, seckillExecution);
            }
        }
        return result;
    }

    /**
     * 获取系统当前时间
     * @param model
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> execute(Model model) {
        return new SeckillResult<Long>(true, System.currentTimeMillis());
    }
}
