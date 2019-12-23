package com.gpdi.operatingunit.service.reportconfig.impl;

import com.alibaba.excel.metadata.Sheet;
import com.gpdi.operatingunit.dao.reportconfig.DataSapListMapper;
import com.gpdi.operatingunit.entity.reportconfig.DataSapListEntity;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.reportconfig.DataSapService;
import com.gpdi.operatingunit.utils.DataGenerateUtils;
import com.gpdi.operatingunit.utils.ExcelUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

/**
 * @Author：long
 * @Date：2019/10/29 9:03
 * @Description：sap数据导入
 */
@Service
public class DataSapServiceIpml implements DataSapService {

    @Autowired
    private DataSapListMapper dataSapListDao;

    @Autowired
    private DataGenerateUtils generateUtils;

    /**
     * description: 读取sheet1
     *
     * @param inputStream
     * @return int
     */
    @Override
    public int dataUpload(InputStream inputStream, InputStream sheetInputStream, String month) {
        // 获取用户信息
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //Integer id = user.getId();
        Long orgCode = user.getTopOrgCode();
        //Long orgCode = dataSapListDao.getUserOrgCode(id);
        Sheet initSheet = new Sheet(1, 0);
        initSheet.setSheetName("sheet");
        List<Object> objectList = ExcelUtils.readMoreThan1000RowBySheet(inputStream, initSheet);
        int count = 0;
        if(!(objectList.size() > 0)){
            return 0;
        }
        List l = (List)objectList.get(0);
        if(!(l.size() == 33)){
            return -1;
        }
        //objectList.remove(0);







        List objList = (List) objectList.get(0);
        Map<String,Integer> map = new HashMap<>();
        List<DataSapListEntity> listEntityList = new ArrayList<>();
        for(int i = 1;i<objectList.size();i++){
            List list = (List) objectList.get(i);
            int tempCount = list.size();
            for(int j = tempCount;j<objList.size();j++){
                list.add(null);
            }
            if(list.size() > 0){

                DataSapListEntity dataSapListEntity = new DataSapListEntity();
                boolean flag = false;
                String per = "";
                if(list.get(1) != null){
                    per = list.get(1).toString();
                }
                if(per.equals(month)){
                    flag = true;
                }
                if("13".equals(month)){
                    flag = true;
                }
                if(flag){
                    // 财年
                    dataSapListEntity.setYear(list.get(0) != null ? Integer.parseInt(list.get(0).toString()) : null);
                    // per
                    dataSapListEntity.setPer(list.get(1) != null ? list.get(1).toString() : null);
                    map.put(dataSapListEntity.getYear() + "_" + dataSapListEntity.getPer(),(dataSapListEntity.getYear() * 100) + Integer.parseInt(dataSapListEntity.getPer()));
                    // 公司
                    dataSapListEntity.setCompany(list.get(2) != null ? list.get(2).toString() : null);
                    // 参考凭证号
                    dataSapListEntity.setCredentialsNo(list.get(3) != null ? list.get(3).toString() : null);
                    // 凭证类
                    dataSapListEntity.setCredentialsTpye(list.get(4) != null ? list.get(4).toString() : null);
                    // 凭证日期
                    dataSapListEntity.setCredentialsDate(list.get(5) != null ? list.get(5).toString() : null);
                    // pk
                    dataSapListEntity.setPk(list.get(6) != null ? list.get(6).toString() : null);
                    // 报表货币值
                    dataSapListEntity.setCurrencyValue(list.get(7) != null ? list.get(7).toString() : null);
                    // 本币
                    dataSapListEntity.setCurrency(list.get(8) != null ? list.get(8).toString() : null);
                    // 利润中心
                    dataSapListEntity.setProfitCenter(list.get(9) != null ? list.get(9).toString() : null);
                    // 利润中心描述
                    dataSapListEntity.setProfitCenterDesc(list.get(10) != null ? list.get(10).toString() : null);
                    // 文本/摘要
                    dataSapListEntity.setText(list.get(11) != null ? list.get(11).toString() : null);
                    // 成本中心
                    dataSapListEntity.setCostCenter(list.get(12) != null ? list.get(12).toString() : null);
                    // 成本中心描述
                    dataSapListEntity.setCostCenterDesc(list.get(13) != null ? list.get(13).toString() : null);
                    // 科目
                    dataSapListEntity.setSubject(list.get(14) != null ? list.get(14).toString() : null);
                    // 年度/月份
                    dataSapListEntity.setYearMonth(list.get(15) != null ? list.get(15).toString() : null);
                    // 成本要素
                    dataSapListEntity.setCostElement(list.get(16) != null ? Long.valueOf(list.get(16).toString()) : null);
                    // 成本要素描述
                    dataSapListEntity.setCostElementDesc(list.get(17) != null ? list.get(17).toString() : null);
                    // 装移
                    dataSapListEntity.setZhuangyi(list.get(18) != null ? list.get(18).toString() : null);
                    // 功能范围
                    dataSapListEntity.setScope(list.get(19) != null ? list.get(19).toString() : null);
                    // 用途
                    dataSapListEntity.setUsage(list.get(20) != null ? list.get(20).toString() : null);
                    // 经济事项
                    dataSapListEntity.setEconomicConsiderations(list.get(21) != null ? list.get(21).toString() : null);
                    // 供应商
                    dataSapListEntity.setSupplier(list.get(22) != null ? list.get(22).toString() : null);
                    // 合同号
                    dataSapListEntity.setContractNo(list.get(23) != null ? list.get(23).toString() : null);
                    // 报账人
                    dataSapListEntity.setOperator(list.get(24) != null ? list.get(24).toString() : null);
                    // 报账单号
                    dataSapListEntity.setSerialNum(list.get(25) != null ? list.get(25).toString() : null);
                    // 参考码1
                    dataSapListEntity.setCustom1(list.get(26) != null ? list.get(26).toString() : null);
                    // 参考码2
                    dataSapListEntity.setCustom2(list.get(27) != null ? list.get(27).toString() : null);
                    // 参考码3
                    dataSapListEntity.setCustom3(list.get(28) != null ? list.get(28).toString() : null);
                    // 组织编码
                    //dataSapListEntity.setOrgCode(list.get(29) != null ? Long.parseLong(list.get(29).toString()) : null);
                    dataSapListEntity.setOrgCode(orgCode);
                    // 营服id
                    dataSapListEntity.setCenterId(list.get(30) != null ? Long.parseLong(list.get(30).toString()) : null);
                    // 月份
                    dataSapListEntity.setMonth(list.get(31) != null ? Integer.getInteger(list.get(31).toString()) : null);
                    // 成本项
                    dataSapListEntity.setCostItem(list.get(32) != null ? list.get(32).toString() : null);
                    // 报账单位
                    //dataSapListEntity.setReimbursementUnit(list.get(33) != null ? list.get(33).toString() : null);
                    listEntityList.add(dataSapListEntity);
                }
            }
        }

        if(listEntityList.size() > 0){
            count = pageSave(listEntityList);
        }

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            Integer yearPer = map.get(key);
            generateUtils.insertRsCostReportValue(yearPer,10001);
            generateUtils.insertQingYuanCountyValue(yearPer,20001);
            System.out.println(yearPer);
        }

        //count += dataUploadSheet(sheetInputStream, orgCode, month);
        return count;
    }

    /**
     * description: 读取sheet2
     *
     * @param inputStream
     * @return int
     */
    public int dataUploadSheet(InputStream inputStream, Long orgCode, int month){
        Sheet initSheet = new Sheet(2, 0);
        initSheet.setSheetName("sheet");
        List<Object> objectList = ExcelUtils.readMoreThan1000RowBySheet(inputStream, initSheet);
        objectList.remove(0);
        List<DataSapListEntity> listEntityList = new ArrayList<>();
        for(Object obj : objectList) {
            List list = (List) obj;
            if (list.size() > 0) {
                DataSapListEntity dataSapListEntity = new DataSapListEntity();
                String dataMonth = list.get(0).toString();
                if(dataMonth.indexOf("0"+month) > 0 || 13 == month){
                    // month
                    dataSapListEntity.setMonth(Integer.valueOf(list.get(0).toString()));
                    // per
                    dataSapListEntity.setPer(list.get(1).toString());
                    // 凭证类
                    dataSapListEntity.setCredentialsTpye(list.get(2).toString());
                    // 参考凭证号
                    dataSapListEntity.setCredentialsNo(list.get(3).toString());
                    // 成本中心
                    dataSapListEntity.setCostCenter(list.get(4).toString());
                    // 利润中心
                    dataSapListEntity.setProfitCenter(list.get(5).toString());
                    // 成本要素
                    dataSapListEntity.setCostElement(Long.valueOf(list.get(6).toString()));
                    // 成本要素描述
                    dataSapListEntity.setCostElementDesc(list.get(7).toString());
                    // 报表货币值
                    dataSapListEntity.setCurrencyValue(list.get(8).toString());
                    // 功能范围
                    dataSapListEntity.setScope(list.get(9).toString());
                    // 名称
                    dataSapListEntity.setText(list.get(10).toString());
                    // 组织编码
                    dataSapListEntity.setOrgCode(orgCode);
                    listEntityList.add(dataSapListEntity);
                }
            }
        }
        return pageSave(listEntityList);
    }

    /**
     * description: sap数据导入
     *
     * @param listEntityList
     * @return int
     */
    public int pageSave(List<DataSapListEntity> listEntityList){
        int count = 0;
        if (listEntityList.size() > 0) {
            //超过1000条进行拆分,1000为一页
            if (listEntityList.size() > 1000) {
                int totalPage = (listEntityList.size() / 1000) + (listEntityList.size() % 1000 == 0 ? 0 : 1);
                for (int i = 0; i < totalPage; i++) {
                    List sublistEntityList = new LinkedList();
                    if (i == totalPage - 1) {
                        sublistEntityList = listEntityList.subList(i * 1000, listEntityList.size());
                    } else {
                        sublistEntityList = listEntityList.subList(i * 1000, (i + 1) * 1000);
                    }
                    count += dataSapListDao.addDataSapList(sublistEntityList);
                }
            } else {
                count += dataSapListDao.addDataSapList(listEntityList);
            }
        }
        return count;
    }
}
