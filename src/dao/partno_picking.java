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
public class partno_picking
{
    private int IDpicking;
    private String PartNo;
    private String PartName;
    
    public partno_picking(){
        
    }
    
    public partno_picking(int IIDpicking, String IPartNo,String IPartName){
        this.IDpicking = IIDpicking;
        this.PartNo = IPartNo;
        this.PartName = IPartName;
    }
    
    public int getIDpicking(){
        return this.IDpicking;
    }
    
    public void setIDpicking(int IIDpicking){
        this.IDpicking = IIDpicking;
    }
    
    public String getPartNo(){
        return this.PartNo;
    }
    
    public void setPartNo(String IPartNo){
        this.PartNo = IPartNo;
    }
    
    public String getPartName(){
        return this.PartName;
    }
    
    public void setPartName(String IPartName){
        this.PartName = IPartName;
    }
}
