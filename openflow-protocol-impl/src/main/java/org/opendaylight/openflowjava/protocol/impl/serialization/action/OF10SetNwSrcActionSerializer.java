/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowjava.protocol.impl.serialization.action;

import io.netty.buffer.ByteBuf;

import org.opendaylight.openflowjava.protocol.impl.util.ActionConstants;
import org.opendaylight.openflowjava.util.ByteBufUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.action.grouping.action.choice.SetNwSrcCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.actions.grouping.Action;

/**
 * @author michal.polkorab
 *
 */
public class OF10SetNwSrcActionSerializer extends AbstractActionSerializer {

    @Override
    public void serialize(final Action action, final ByteBuf outBuffer) {
        super.serialize(action, outBuffer);
        Iterable<String> addressGroups = ByteBufUtils.DOT_SPLITTER
                .split(((SetNwSrcCase) action.getActionChoice()).getSetNwSrcAction()
                        .getIpAddress().getValue());
        for (String group : addressGroups) {
            outBuffer.writeByte(Short.parseShort(group));
        }
    }

    @Override
    protected int getType() {
        return ActionConstants.SET_NW_SRC_CODE;
    }

    @Override
    protected int getLength() {
        return ActionConstants.GENERAL_ACTION_LENGTH;
    }
}
