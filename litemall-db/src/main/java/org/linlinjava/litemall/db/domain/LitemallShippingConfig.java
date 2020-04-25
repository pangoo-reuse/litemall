package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class LitemallShippingConfig {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    public static final Boolean IS_DELETED = Deleted.IS_DELETED.value();

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    public static final Boolean NOT_DELETED = Deleted.NOT_DELETED.value();

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.express_freight_min
     *
     * @mbg.generated
     */
    private BigDecimal expressFreightMin;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.freight_value
     *
     * @mbg.generated
     */
    private BigDecimal freightValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.region_id
     *
     * @mbg.generated
     */
    private Integer regionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.region_code
     *
     * @mbg.generated
     */
    private Integer regionCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.region_address
     *
     * @mbg.generated
     */
    private String regionAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.enabled
     *
     * @mbg.generated
     */
    private Boolean enabled;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.created_time
     *
     * @mbg.generated
     */
    private LocalDateTime createdTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_shipping_config.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.id
     *
     * @return the value of litemall_shipping_config.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.id
     *
     * @param id the value for litemall_shipping_config.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.express_freight_min
     *
     * @return the value of litemall_shipping_config.express_freight_min
     *
     * @mbg.generated
     */
    public BigDecimal getExpressFreightMin() {
        return expressFreightMin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.express_freight_min
     *
     * @param expressFreightMin the value for litemall_shipping_config.express_freight_min
     *
     * @mbg.generated
     */
    public void setExpressFreightMin(BigDecimal expressFreightMin) {
        this.expressFreightMin = expressFreightMin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.freight_value
     *
     * @return the value of litemall_shipping_config.freight_value
     *
     * @mbg.generated
     */
    public BigDecimal getFreightValue() {
        return freightValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.freight_value
     *
     * @param freightValue the value for litemall_shipping_config.freight_value
     *
     * @mbg.generated
     */
    public void setFreightValue(BigDecimal freightValue) {
        this.freightValue = freightValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.region_id
     *
     * @return the value of litemall_shipping_config.region_id
     *
     * @mbg.generated
     */
    public Integer getRegionId() {
        return regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.region_id
     *
     * @param regionId the value for litemall_shipping_config.region_id
     *
     * @mbg.generated
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.region_code
     *
     * @return the value of litemall_shipping_config.region_code
     *
     * @mbg.generated
     */
    public Integer getRegionCode() {
        return regionCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.region_code
     *
     * @param regionCode the value for litemall_shipping_config.region_code
     *
     * @mbg.generated
     */
    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.region_address
     *
     * @return the value of litemall_shipping_config.region_address
     *
     * @mbg.generated
     */
    public String getRegionAddress() {
        return regionAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.region_address
     *
     * @param regionAddress the value for litemall_shipping_config.region_address
     *
     * @mbg.generated
     */
    public void setRegionAddress(String regionAddress) {
        this.regionAddress = regionAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.enabled
     *
     * @return the value of litemall_shipping_config.enabled
     *
     * @mbg.generated
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.enabled
     *
     * @param enabled the value for litemall_shipping_config.enabled
     *
     * @mbg.generated
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.created_time
     *
     * @return the value of litemall_shipping_config.created_time
     *
     * @mbg.generated
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.created_time
     *
     * @param createdTime the value for litemall_shipping_config.created_time
     *
     * @mbg.generated
     */
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.update_time
     *
     * @return the value of litemall_shipping_config.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.update_time
     *
     * @param updateTime the value for litemall_shipping_config.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    public void andLogicalDeleted(boolean deleted) {
        setDeleted(deleted ? Deleted.IS_DELETED.value() : Deleted.NOT_DELETED.value());
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_shipping_config.deleted
     *
     * @return the value of litemall_shipping_config.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_shipping_config.deleted
     *
     * @param deleted the value for litemall_shipping_config.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", expressFreightMin=").append(expressFreightMin);
        sb.append(", freightValue=").append(freightValue);
        sb.append(", regionId=").append(regionId);
        sb.append(", regionCode=").append(regionCode);
        sb.append(", regionAddress=").append(regionAddress);
        sb.append(", enabled=").append(enabled);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LitemallShippingConfig other = (LitemallShippingConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExpressFreightMin() == null ? other.getExpressFreightMin() == null : this.getExpressFreightMin().equals(other.getExpressFreightMin()))
            && (this.getFreightValue() == null ? other.getFreightValue() == null : this.getFreightValue().equals(other.getFreightValue()))
            && (this.getRegionId() == null ? other.getRegionId() == null : this.getRegionId().equals(other.getRegionId()))
            && (this.getRegionCode() == null ? other.getRegionCode() == null : this.getRegionCode().equals(other.getRegionCode()))
            && (this.getRegionAddress() == null ? other.getRegionAddress() == null : this.getRegionAddress().equals(other.getRegionAddress()))
            && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExpressFreightMin() == null) ? 0 : getExpressFreightMin().hashCode());
        result = prime * result + ((getFreightValue() == null) ? 0 : getFreightValue().hashCode());
        result = prime * result + ((getRegionId() == null) ? 0 : getRegionId().hashCode());
        result = prime * result + ((getRegionCode() == null) ? 0 : getRegionCode().hashCode());
        result = prime * result + ((getRegionAddress() == null) ? 0 : getRegionAddress().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    public enum Deleted {
        NOT_DELETED(new Boolean("0"), "未删除"),
        IS_DELETED(new Boolean("1"), "已删除");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final Boolean value;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final String name;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        Deleted(Boolean value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public Boolean value() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table litemall_shipping_config
     *
     * @mbg.generated
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        expressFreightMin("express_freight_min", "expressFreightMin", "DECIMAL", false),
        freightValue("freight_value", "freightValue", "DECIMAL", false),
        regionId("region_id", "regionId", "INTEGER", false),
        regionCode("region_code", "regionCode", "INTEGER", false),
        regionAddress("region_address", "regionAddress", "VARCHAR", false),
        enabled("enabled", "enabled", "BIT", false),
        createdTime("created_time", "createdTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        deleted("deleted", "deleted", "BIT", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table litemall_shipping_config
         *
         * @mbg.generated
         */
        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}