/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2019-03-13 09:58:24  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.core.entity;

import org.apache.ibatis.type.Alias;

import com.suixingpay.takin.mybatis.annotation.TableId;
import com.suixingpay.takin.mybatis.enums.IdType;
import com.suixingpay.takin.mybatis.entity.GenericPo;
import com.suixingpay.takin.mybatis.annotation.TableField;
import com.suixingpay.takin.mybatis.annotation.TableName;
import java.util.Date;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

 /**  
 * 公共数据源配置表 实体Entity
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Alias("PublicDataSource")
@TableName("b_public_data_source")
public class PublicDataSource extends GenericPo<Long>{

    private static final long serialVersionUID = 1L;

    /**主键.*/
    @TableId(value="id", type= IdType.AUTO)
    private Long id;	

    /**数据源编码.*/
    @NotNull
    private String code;	

    /**数据源名称.*/
    private String name;	

    /**数据源xml文本.*/
    @TableField("xml_text")
    private String xmlText;	

    /**数据源json文本.*/
    @TableField("json_text")
    private String jsonText;	

    /**数据源说明.*/
    private String declares;	

    /**创建人.*/
    private Long creator;	

    /**创建时间.*/
    @TableField("create_time")
    private Date createTime;	

    /**状态.*/
    private Integer state;	

    /**类型.*/
    private Integer type;	

    /**是否作废.*/
    private Integer iscancel;	

    /**备注.*/
    private String remark;	

    /**定义字段名.*/
    public interface Property extends GenericPo.Property {
        String id = "id";
        String code = "code";
        String name = "name";
        String xmlText = "xmlText";
        String jsonText = "jsonText";
        String declares = "declares";
        String creator = "creator";
        String createTime = "createTime";
        String state = "state";
        String type = "type";
        String iscancel = "iscancel";
        String remark = "remark";
    }
}
