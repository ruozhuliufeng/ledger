package cn.aixuxi.ledger.service.impl;

import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.dto.LedgerReportDTO;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.enums.TransactionTypeEnum;
import cn.aixuxi.ledger.mapper.LedgerRecordMapper;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.utils.SecureUtil;
import cn.aixuxi.ledger.utils.ZipUtil;
import cn.aixuxi.ledger.vo.LedgerQuery;
import cn.aixuxi.ledger.vo.LedgerReportVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class LedgerRecordServiceImpl extends ServiceImpl<LedgerRecordMapper, LedgerRecord>
        implements LedgerRecordService {
    private final LedgerUserService userService;
    private final SecureUtil secureUtil;

    @SneakyThrows
    @Override
    public void importRecordByThird(MultipartFile file, String password) {
        // 登录用户
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        // 获取文件类型，取后缀
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
        // 获取当前路径
        String path = new File((ResourceUtils.getURL("classpath:").getPath())).getAbsolutePath();
        String filePath = path + File.separator + LedgerConstant.UPLOAD_PATH_PREFIX_STATIC + File.separator + LedgerConstant.UPLOAD_PATH_PREFIX_UPLOAD_FILE + File.separator;
        File temp = new File(filePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        // 临时文件
        File localFile = new File(filePath + filename);
        // 把上传的文件保存到本地
        file.transferTo(localFile);
        log.info(filename + " 上传成功");
        List<File> fileList = new ArrayList<>();
        // 如果是压缩文件，获取解压文件
        if (suffix.equals(LedgerConstant.FILE_TYPE_ZIP)) {
            fileList.addAll(ZipUtil.unzipFile(localFile, filePath, password));
        } else {
            fileList.add(localFile);
        }
        List<LedgerRecord> recordList = new ArrayList<>();
        for (File importFile : fileList) {
            CsvReader reader = new CsvReader(importFile, CsvReadConfig.defaultConfig());
            List<CsvRow> rows = reader.read().getRows();
            // 微信账单处理
            if (importFile.getName().startsWith(LedgerConstant.LEDGER_WECHAT)) {
                recordList = transformByWeChat(rows, user.getId());
            } else if (importFile.getName().startsWith(LedgerConstant.LEDGER_ALIPAY)) {
                recordList = transformByAliPay(rows, user.getId());
            }
            // 保存数据
            saveOrUpdateRecordList(recordList, 1000);
            // 删除文件
            FileUtil.del(importFile);
        }
        FileUtil.del(localFile);
        try {
            // 删除空目录
            for (File f : FileUtil.ls(filePath)) {
                if (FileUtil.isDirEmpty(f)) {
                    FileUtil.del(f);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 更新或保存数据
     *
     * @param recordList 交易记录数据
     * @param batchSize  批量处理数据
     */
    private boolean saveOrUpdateRecordList(List<LedgerRecord> recordList, int batchSize) {
        if (CollUtil.isEmpty(recordList)) {
            return false;
        }
        batchSize = batchSize == 0 ? 1000 : batchSize;
        int totalSize = recordList.size();
        int mod = totalSize / batchSize;
        // 批量处理
        for (int i = 0; i < mod + 1; i++) {
            if (i == mod) {
                this.baseMapper.saveOrUpdateRecordList(recordList.subList(i * batchSize, totalSize));
            } else {
                this.baseMapper.saveOrUpdateRecordList(recordList.subList(i * batchSize, (i + 1) * batchSize));
            }

        }
        return true;
    }

    /**
     * 新建交易记录
     *
     * @param record 记录信息
     */
    @Override
    public boolean saveRecord(LedgerRecord record) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        record.setUserId(user.getId());
        if (StrUtil.isBlank(record.getTransactionSn())) {
            // 当前时间精确到毫秒，防止他人创建时相同
            String transactionSn = new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSS").format(System.currentTimeMillis());
            record.setTransactionSn(transactionSn);
        }
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        return this.save(record);
    }

    /**
     * 查询报表信息
     *
     * @return 报表信息
     */
    @Override
    public LedgerReportVO queryReport() {
        LedgerReportVO reportVO = new LedgerReportVO();
        Long userId = secureUtil.getUserId();
        List<LedgerReportDTO> incomeRatioList = this.baseMapper.queryIncomeRatioReport(userId);
        List<LedgerReportDTO> expenseRatioList = this.baseMapper.queryExpenseRatioReport(userId);
        List<LedgerReportDTO> otherRatioList = this.baseMapper.queryOtherRatioReport(userId);
        List<LedgerReportDTO> recentIncomeList = this.baseMapper.queryRecentIncomeReport(userId);
        List<LedgerReportDTO> recentExpenseList = this.baseMapper.queryRecentExpenseReport(userId);
        List<LedgerReportDTO> recentOtherList = this.baseMapper.queryRecentOtherReport(userId);
        reportVO.setIncomeRatioList(incomeRatioList);
        reportVO.setExpenseRatioList(expenseRatioList);
        reportVO.setOtherRatioList(otherRatioList);
        reportVO.setRecentIncomeList(recentIncomeList);
        reportVO.setRecentExpenseList(recentExpenseList);
        reportVO.setRecentOtherList(recentOtherList);
        return reportVO;
    }

    /**
     * 支付宝账单转换
     *
     * @param rows   导入支付宝账单数据
     * @param userId 操作用户
     * @return 转换数据
     */
    @SneakyThrows
    private List<LedgerRecord> transformByAliPay(List<CsvRow> rows, Long userId) {
        List<LedgerRecord> recordList = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            CsvRow csvRow = rows.get(i);
//            if (csvRow.size() != 12) {
//                break;
//            }
            LedgerRecord record = new LedgerRecord();
            // 收/支
            if (StrUtil.isBlank(csvRow.get(0))) {
                record.setTransactionType(TransactionTypeEnum.OTHER.getCode());
            } else {
                if (StrUtil.equals(csvRow.get(0).trim(), TransactionTypeEnum.INCOME.getMessage())) {
                    record.setTransactionType(TransactionTypeEnum.INCOME.getCode());
                } else if (StrUtil.equals(csvRow.get(0).trim(), TransactionTypeEnum.EXPEND.getMessage())) {
                    record.setTransactionType(TransactionTypeEnum.EXPEND.getCode());
                } else if (StrUtil.equals(csvRow.get(0).trim(), TransactionTypeEnum.OTHER.getMessage())) {
                    record.setTransactionType(TransactionTypeEnum.OTHER.getCode());
                } else {
                    record.setTransactionType(TransactionTypeEnum.OTHER.getCode());
                }
            }
            // 交易对方
            record.setCounterpartyName(csvRow.get(1));
            // 对方账户
            record.setCounterpartyAccount(csvRow.get(2));
            // 商品说明
            record.setProductName(csvRow.get(3));
            // 收/付款方式
            record.setPaymentMethod(csvRow.get(4));
            // 金额
            record.setAmount(new BigDecimal(csvRow.get(5).trim()));
            // 交易状态
            record.setCurrentStatus(csvRow.get(6));
            // 交易分类
            record.setTransactionCategory(csvRow.get(7));
            // 交易订单号
            record.setTransactionSn(csvRow.get(8).trim());
            // 商家订单号
            record.setMerchantOrderSn(csvRow.get(9).trim());
            // 交易时间
            if (StrUtil.isNotBlank(csvRow.get(10))) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                record.setTransactionTime(simpleDateFormat.parse(csvRow.get(10)));
            }
            record.setUserId(userId);
            record.setCreateTime(new Date());
            record.setCreateUser(userId);
            record.setUpdateTime(new Date());
            record.setUpdateUser(userId);
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * 微信账单数据转换
     *
     * @param rows   导入数据
     * @param userId 操作用户
     * @return 转换后的数据
     */
    @SneakyThrows
    private List<LedgerRecord> transformByWeChat(List<CsvRow> rows, Long userId) {
        List<LedgerRecord> recordList = new ArrayList<>();
        for (int i = 17; i < rows.size(); i++) {
            CsvRow csvRow = rows.get(i);
            if (csvRow.size() != 11) {
                break;
            }

            LedgerRecord record = new LedgerRecord();
            record.setUserId(userId);
            // 交易时间
            if (StrUtil.isNotBlank(csvRow.get(0))) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                record.setTransactionTime(simpleDateFormat.parse(csvRow.get(0)));
            }
            // 交易类型
            record.setTransactionCategory(csvRow.get(1));
            // 交易对方
            record.setCounterpartyName(csvRow.get(2));
            // 商品
            record.setProductName(csvRow.get(3));
            // 收/支
            if (StrUtil.isBlank(csvRow.get(4))) {
                record.setTransactionType(TransactionTypeEnum.OTHER.getCode());
            } else {
                if (StrUtil.equals(csvRow.get(4).trim(), TransactionTypeEnum.INCOME.getMessage())) {
                    record.setTransactionType(TransactionTypeEnum.INCOME.getCode());
                } else if (StrUtil.equals(csvRow.get(4).trim(), TransactionTypeEnum.EXPEND.getMessage())) {
                    record.setTransactionType(TransactionTypeEnum.EXPEND.getCode());
                } else {
                    record.setTransactionType(TransactionTypeEnum.OTHER.getCode());
                }
            }
            // 金额(元)
            record.setAmount(new BigDecimal(csvRow.get(5).trim().substring(1)));
            // 支付方式
            record.setPaymentMethod(csvRow.get(6));
            // 当期状态
            record.setCurrentStatus(csvRow.get(7));
            // 交易单号
            record.setTransactionSn(csvRow.get(8).trim());
            // 商户单号
            record.setMerchantOrderSn(csvRow.get(9).trim());
            // 备注
            record.setRemark(csvRow.get(10));
            record.setCreateTime(new Date());
            record.setCreateUser(userId);
            record.setUpdateTime(new Date());
            record.setUpdateUser(userId);
            recordList.add(record);
        }
        return recordList;
    }
}
