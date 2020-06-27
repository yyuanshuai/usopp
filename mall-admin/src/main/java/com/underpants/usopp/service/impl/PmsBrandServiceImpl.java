package com.underpants.usopp.service.impl;

import com.github.pagehelper.PageHelper;
import com.underpants.usopp.dto.PmsBrandParam;
import com.underpants.usopp.mapper.PmsBrandMapper;
import com.underpants.usopp.mapper.PmsProductMapper;
import com.underpants.usopp.model.PmsBrand;
import com.underpants.usopp.model.PmsBrandExample;
import com.underpants.usopp.model.PmsProduct;
import com.underpants.usopp.model.PmsProductExample;
import com.underpants.usopp.service.PmsBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {
    @Autowired
    private PmsBrandMapper brandMapper;

    @Autowired
    private PmsProductMapper productMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        if(StringUtils.isEmpty(pmsBrand.getFirstLetter())){
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0,1));
        }
        return brandMapper.insertSelective(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        if(StringUtils.isEmpty(pmsBrand.getFirstLetter())){
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0,1));
        }
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setBrandName(pmsBrand.getName());
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andBrandIdEqualTo(id);
        productMapper.updateByExampleSelective(pmsProduct, example);
        return brandMapper.updateByPrimaryKeySelective(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        return brandMapper.deleteByExample(example);
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandExample example = new PmsBrandExample();
        example.setOrderByClause("sort desc");
        PmsBrandExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andNameLike("%"+keyword+"%");
        }
        return brandMapper.selectByExample(example);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        return brandMapper.updateByExampleSelective(pmsBrand, example);
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        return brandMapper.updateByExampleSelective(pmsBrand, example);
    }
}
