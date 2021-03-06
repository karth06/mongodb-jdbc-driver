package com.dbschema.schema;


import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Wise Coders Gmbh. BSD License-3. Free to use, distribution forbidden. Improvements of the driver accepted only in https://bitbucket.org/dbschema/mongodb-jdbc-driver.
 */
public class MetaField {

    public final MetaJson parentJson;
    public final String name, typeName;
    public final List<ObjectId> objectIds = new ArrayList<ObjectId>();
    public final int type;
    public final List<MetaReference> references = new ArrayList<MetaReference>();
    private boolean mandatory = true;


    MetaField(final MetaJson parentJson, final String name, final String typeName, int type ){
        this.parentJson = parentJson;
        this.name = name;
        this.typeName = typeName;
        this.type = type;
    }

    void addObjectId(ObjectId objectId){
        if ( objectIds.size() < 4 ){
            objectIds.add( objectId );
        }
    }

    public String getNameWithPath(){
        return ( parentJson != null && !(parentJson instanceof MetaCollection ) ? parentJson.getNameWithPath() + "." + name : name );
    }

    public String getPkColumnName(){
        String pkColumnName = name;
        final int idx = pkColumnName.lastIndexOf('.');
        if ( idx > 0 ){
            pkColumnName = pkColumnName.substring( idx + 1 );
        }
        return pkColumnName;
    }

    public MetaReference createReferenceTo(MetaCollection pkCollection){
        MetaReference ifk = new MetaReference( this, pkCollection );
        references.add( ifk );
        return ifk;
    }

    public MetaCollection getMetaCollection(){
        MetaField field = this;
        while ( field != null && !( field instanceof MetaCollection )){
            field = field.parentJson;
        }
        return (MetaCollection)field;
    }

    public void collectFieldsWithObjectId(List<MetaField> unsolvedFields) {
        if ( !objectIds.isEmpty() ){
            unsolvedFields.add(this);
        }
    }

    @Override
    public String toString() {
        return getNameWithPath();
    }
    void setMandatory( boolean mandatory ){
        this.mandatory = mandatory;
    }

    public boolean isMandatory(){
        return mandatory;
    }
}
