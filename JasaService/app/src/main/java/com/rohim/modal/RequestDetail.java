package com.rohim.modal;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Nurochim on 19/10/2016.
 */

@DatabaseTable(tableName = RequestDetail.tbl_request_detail)
public class RequestDetail implements Serializable {
    public static final String tbl_request_detail = "TBL_REQUEST_DETAIL";
    public static final String clm_id_request_detail = "ID_REQUEST_DETAIL";
    public static final String clm_fid_request = "FID_REQUEST";
    public static final String clm_fid_service_item = "FID_SERVICE_ITEM";
    public static final String clm_service_item_name = "SERVICE_ITEM_NAME";
    public static final String clm_service_item_value = "SERVICE_ITEM_VALUE";
    public static final String clm_jenis_input = "JENIS_INPUT";
    public static final String clm_satuan = "SATUAN";

    @DatabaseField(id = true, columnName = clm_id_request_detail)
    String idRequestDetail;
    @DatabaseField(columnName = clm_fid_request)
    String fidRequest;
    @DatabaseField(columnName = clm_fid_service_item, dataType = DataType.INTEGER)
    int fidServiceItem;
    @DatabaseField(columnName = clm_service_item_name)
    String serviceItemName;
    @DatabaseField(columnName = clm_service_item_value)
    String serviceItemValue;
    @DatabaseField(columnName = clm_jenis_input)
    String jenisInput;
    @DatabaseField(columnName = clm_satuan)
    String satuan;

    public RequestDetail(){
    }

    public RequestDetail(ServiceItem serviceItem){
        this.fidServiceItem = serviceItem.getIdServiceItem();
        this.serviceItemName = serviceItem.getItemName();
        this.jenisInput = serviceItem.getJenisInput();
    }

    public String getIdRequestDetail() {
        return idRequestDetail;
    }

    public void setIdRequestDetail(String idRequestDetail) {
        this.idRequestDetail = idRequestDetail;
    }

    public String getFidRequest() {
        return fidRequest;
    }

    public void setFidRequest(String fidRequest) {
        this.fidRequest = fidRequest;
    }

    public int getFidServiceItem() {
        return fidServiceItem;
    }

    public void setFidServiceItem(int fidServiceItem) {
        this.fidServiceItem = fidServiceItem;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceItemValue() {
        return serviceItemValue;
    }

    public void setServiceItemValue(String serviceItemValue) {
        this.serviceItemValue = serviceItemValue;
    }

    public String getJenisInput() {
        return jenisInput;
    }

    public void setJenisInput(String jenisInput) {
        this.jenisInput = jenisInput;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
