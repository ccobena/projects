/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:info@pro-ids.com">ProIDS sp. z o.o.</a>
 *
 */
public interface CAPServiceCircuitSwitchedCall extends CAPServiceBase {

    CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress, Long localTrId) throws CAPException;

    CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress) throws CAPException;

    void addCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener);

    void removeCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener);

    /**
     * Create new structured dialog with default TransactionId.
     * Used for SCCP relay functionality
     * @param relayedLocalTrId
     */
    CAPDialogCircuitSwitchedCall createNewRelayedDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long relayedLocalTrId) throws CAPException;

}