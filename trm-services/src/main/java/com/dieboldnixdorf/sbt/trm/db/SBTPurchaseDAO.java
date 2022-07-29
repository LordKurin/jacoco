package com.dieboldnixdorf.sbt.trm.db;

import com.myproclassic.server.db.sql.PCEDBException;
import com.myproclassic.server.dblayer.dao.IPCEDataAccessObject;
import com.myproclassic.server.dblayer.dao.PCEDataAccessObject;

public class SBTPurchaseDAO extends SBTPurchaseDTO implements IPCEDataAccessObject {

    private static final long serialVersionUID = -9019026662919739723L;

    /** The dao. */
    private static PCEDataAccessObject<SBTPurchaseDAO> dao = new PCEDataAccessObject<SBTPurchaseDAO>(SBTPurchaseDAO.DESC);

    public static SBTPurchaseDAO downcast(SBTPurchaseDTO dto) {
        if (dto instanceof SBTPurchaseDAO) {
            return (SBTPurchaseDAO) dto;
        }
        return new SBTPurchaseDAO().setDTO(dto);
    }

    public static SBTPurchaseDTO findByPrimaryKey(String customerId) throws PCEDBException {
        return dao.selectByPrimaryKey(customerId);
    }

    @Override
    public int insert() throws PCEDBException {
        return dao.insert(this);
    }

    @Override
    public int update() throws PCEDBException {
        return dao.update(this);
    }

    @Override
    public int delete() throws PCEDBException {
        return dao.delete(this);
    }

}