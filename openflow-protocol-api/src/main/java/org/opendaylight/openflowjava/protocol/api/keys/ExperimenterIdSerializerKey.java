/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowjava.protocol.api.keys;

import org.opendaylight.yangtools.yang.binding.DataObject;

/**
 * @author michal.polkorab
 * @param <T> class of object to be serialized
 */
public class ExperimenterIdSerializerKey<T extends DataObject> extends MessageTypeKey<T>
        implements ExperimenterSerializerKey {

    private Long experimenterId;

    /**
     * @param msgVersion protocol wire version
     * @param experimenterId experimenter / vendor ID
     * @param objectClass class of object to be serialized
     */
    public ExperimenterIdSerializerKey(short msgVersion,
            Long experimenterId, Class<T> objectClass) {
        super(msgVersion, objectClass);
        this.experimenterId = experimenterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((experimenterId == null) ? 0 : experimenterId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ExperimenterIdSerializerKey)) {
            return false;
        }
        ExperimenterIdSerializerKey<?> other = (ExperimenterIdSerializerKey<?>) obj;
        if (experimenterId == null) {
            if (other.experimenterId != null) {
                return false;
            }
        } else if (!experimenterId.equals(other.experimenterId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " experimenterID: " + experimenterId;
    }
}