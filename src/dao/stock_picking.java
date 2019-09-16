/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author lupa-nama
 */
public class stock_picking
{
    private String PartNo;
    private int QtyStock;
    
    public stock_picking(){
        
    }
    
    public stock_picking(String IPartNo,int IQtyStock){
        this.PartNo = IPartNo;
        this.QtyStock = IQtyStock;
    }
    
    public String getPartNo(){
        return this.PartNo;
    }
    
    public void setPartNo(String IPartNo){
        this.PartNo = IPartNo;
    }
    
    public int getQtyStock(){
        return QtyStock;
    }
    
    public void setQtyStock(int IQtyStock){
        this.QtyStock = IQtyStock;
    }
    
}
