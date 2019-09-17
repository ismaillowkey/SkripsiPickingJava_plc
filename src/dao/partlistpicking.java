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
public class partlistpicking
{
    private String PartKanban;
    private int seq;
    private String PartNo;
    private String PartName;
    private int IDpicking;
    private int qty;
    private int QtyStock;

    public partlistpicking(int Iseq, String IPartNo, String IPartName, int IIDpicking, int Iqty, int IQtyStock)
    {
        this.seq = Iseq;
        this.PartNo = IPartNo;
        this.PartName = IPartName;
        this.IDpicking = IIDpicking;
        this.qty = Iqty;
        this.QtyStock = IQtyStock;
    }

    public partlistpicking(String IPartKanban, int Iseq, int IIDpicking, int Iqty)
    {
        this.PartKanban = IPartKanban;
        this.seq = Iseq;
        this.IDpicking = IIDpicking;
        this.qty = Iqty;
    }

    public partlistpicking()
    {

    }

    public String getPartKanban()
    {
        return PartKanban;
    }
        
       
        
    public int getSeq()
    {
        return this.seq;
    }

    public String getPartNo()
    {
        return this.PartNo;
    }

    public String getPartName()
    {
        return this.PartName;
    }

    public int getIDpicking()
    {
        return this.IDpicking;
    }

    public int getQty()
    {
        return this.qty;
    }
     
    public int getQtyStock()
    {
        return this.QtyStock;
    }
}
      