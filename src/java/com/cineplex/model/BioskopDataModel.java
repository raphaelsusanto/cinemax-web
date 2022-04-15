/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.model;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author raphael
 */
public class BioskopDataModel extends ListDataModel<Bioskop> implements SelectableDataModel<Bioskop> {

    public BioskopDataModel(List<Bioskop> list) {
        super(list);
    }

    public BioskopDataModel() {
    }

    @Override
    public Object getRowKey(Bioskop t) {
       return t.getIdBioskop();
    }

    @Override
    public Bioskop getRowData(String string) {
       Bioskop b= new BioskopDAO().getById(Integer.parseInt(string));
       return b;
    }
    
}
