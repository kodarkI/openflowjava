/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowjava.protocol.api.keys.experimenter;

import org.opendaylight.openflowjava.protocol.api.extensibility.MessageTypeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.instruction.rev130731.Experimenter;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.instruction.rev130731.instructions.grouping.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.InstructionBase;

/**
 * @author michal.polkorab
 */
public class ExperimenterInstructionSerializerKey extends MessageTypeKey<Instruction>
        implements ExperimenterSerializerKey {

    private Class<? extends InstructionBase> instructionType;
    private Long experimenterId;

    /**
     * @param msgVersion protocol wire version
     * @param experimenterId experimenter / vendor ID
     */
    public ExperimenterInstructionSerializerKey(short msgVersion, Long experimenterId) {
        super(msgVersion, Instruction.class);
        this.instructionType = Experimenter.class;
        this.experimenterId = experimenterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExperimenterInstructionSerializerKey other = (ExperimenterInstructionSerializerKey) obj;
        if (instructionType == null) {
            if (other.instructionType != null)
                return false;
        } else if (!instructionType.equals(other.instructionType))
            return false;
        if (experimenterId == null) {
            if (other.experimenterId != null)
                return false;
        } else if (!experimenterId.equals(other.experimenterId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " instructionType type: " + instructionType.getName()
                + " vendorID: " + experimenterId;
    }
}