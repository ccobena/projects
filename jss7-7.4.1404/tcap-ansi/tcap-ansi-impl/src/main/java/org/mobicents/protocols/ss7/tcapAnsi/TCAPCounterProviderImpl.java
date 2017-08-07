/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.tcapAnsi;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.mobicents.protocols.ss7.statistics.StatDataCollectionImpl;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPCounterProvider;

/**
*
* @author sergey vetyutnev
*
*/
public class TCAPCounterProviderImpl implements TCAPCounterProvider {

    private UUID sessionId = UUID.randomUUID();

    private TCAPProviderImpl provider;
    private StatDataCollection statDataCollection = new StatDataCollectionImpl();

    private AtomicLong tcUniReceivedCount = new AtomicLong();
    private AtomicLong tcUniSentCount = new AtomicLong();
    private AtomicLong tcQueryReceivedCount = new AtomicLong();
    private AtomicLong tcQuerySentCount = new AtomicLong();
    private AtomicLong tcConversationReceivedCount = new AtomicLong();
    private AtomicLong tcConversationSentCount = new AtomicLong();
    private AtomicLong tcResponseReceivedCount = new AtomicLong();
    private AtomicLong tcResponseSentCount = new AtomicLong();
    private AtomicLong tcPAbortReceivedCount = new AtomicLong();
    private AtomicLong tcPAbortSentCount = new AtomicLong();
    private AtomicLong tcUserAbortReceivedCount = new AtomicLong();
    private AtomicLong tcUserAbortSentCount = new AtomicLong();

    private AtomicLong invokeLastReceivedCount = new AtomicLong();
    private AtomicLong invokeNotLastReceivedCount = new AtomicLong();
    private AtomicLong invokeLastSentCount = new AtomicLong();
    private AtomicLong invokeNotLastSentCount = new AtomicLong();
    private AtomicLong returnResultNotLastReceivedCount = new AtomicLong();
    private AtomicLong returnResultNotLastSentCount = new AtomicLong();
    private AtomicLong returnResultLastReceivedCount = new AtomicLong();
    private AtomicLong returnResultLastSentCount = new AtomicLong();
    private AtomicLong returnErrorReceivedCount = new AtomicLong();
    private AtomicLong returnErrorSentCount = new AtomicLong();
    private AtomicLong rejectReceivedCount = new AtomicLong();
    private AtomicLong rejectSentCount = new AtomicLong();

    private AtomicLong dialogTimeoutCount = new AtomicLong();
    private AtomicLong dialogReleaseCount = new AtomicLong();

    private AtomicLong allEstablishedDialogsCount = new AtomicLong();
    private AtomicLong allLocalEstablishedDialogsCount = new AtomicLong();
    private AtomicLong allRemoteEstablishedDialogsCount = new AtomicLong();

    private AtomicLong allDialogsDuration = new AtomicLong();

    private static String OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME = "outgoingDialogsPerApplicationContextName";
    private static String INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME = "incomingDialogsPerApplicationContextName";
    private static String OUTGOING_INVOKES_PER_OPERATION_CODE = "outgoingInvokesPerOperationCode";
    private static String INCOMING_INVOKES_PER_OPERATION_CODE = "incomingInvokesPerOperationCode";
    private static String OUTGOING_ERRORS_PER_ERROR_CODE = "outgoingErrorsPerErrorCode";
    private static String INCOMING_ERRORS_PER_ERROR_CODE = "incomingErrorsPerErrorCode";
    private static String OUTGOING_REJECT_PER_PROBLEM = "outgoingRejectPerProblem";
    private static String INCOMING_REJECT_PER_PROBLEM = "incomingRejectPerProblem";

    private static String MIN_DIALOGS_COUNT = "MinDialogsCount";
    private static String MAX_DIALOGS_COUNT = "MaxDialogsCount";

    public TCAPCounterProviderImpl(TCAPProviderImpl provider) {
        this.provider = provider;

        this.statDataCollection.registerStatCounterCollector(MIN_DIALOGS_COUNT, StatDataCollectorType.MIN);
        this.statDataCollection.registerStatCounterCollector(MAX_DIALOGS_COUNT, StatDataCollectorType.MAX);

        this.statDataCollection.registerStatCounterCollector(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_INVOKES_PER_OPERATION_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_INVOKES_PER_OPERATION_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_ERRORS_PER_ERROR_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_ERRORS_PER_ERROR_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_REJECT_PER_PROBLEM, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_REJECT_PER_PROBLEM, StatDataCollectorType.StringLongMap);
    }


    @Override
    public UUID getSessionId() {
        return sessionId;
    }


    @Override
    public long getTcUniReceivedCount() {
        return tcUniReceivedCount.get();
    }

    public void updateTcUniReceivedCount() {
        tcUniReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcUniSentCount() {
        return tcUniSentCount.get();
    }

    public void updateTcUniSentCount() {
        tcUniSentCount.addAndGet(1);
    }

    @Override
    public long getTcQueryReceivedCount() {
        return tcQueryReceivedCount.get();
    }

    public void updateTcQueryReceivedCount() {
        tcQueryReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcQuerySentCount() {
        return tcQuerySentCount.get();
    }

    public void updateTcQuerySentCount() {
        tcQuerySentCount.addAndGet(1);
    }

    @Override
    public long getTcConversationReceivedCount() {
        return tcConversationReceivedCount.get();
    }

    public void updateTcConversationReceivedCount() {
        tcConversationReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcConversationSentCount() {
        return tcConversationSentCount.get();
    }

    public void updateTcConversationSentCount() {
        tcConversationSentCount.addAndGet(1);
    }

    @Override
    public long getTcResponseReceivedCount() {
        return tcResponseReceivedCount.get();
    }

    public void updateTcResponseReceivedCount() {
        tcResponseReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcResponseSentCount() {
        return tcResponseSentCount.get();
    }

    public void updateTcResponseSentCount() {
        tcResponseSentCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortReceivedCount() {
        return tcPAbortReceivedCount.get();
    }

    public void updateTcPAbortReceivedCount() {
        tcPAbortReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortSentCount() {
        return tcPAbortSentCount.get();
    }

    public void updateTcPAbortSentCount() {
        tcPAbortSentCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortReceivedCount() {
        return tcUserAbortReceivedCount.get();
    }

    public void updateTcUserAbortReceivedCount() {
        tcUserAbortReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortSentCount() {
        return tcUserAbortSentCount.get();
    }

    public void updateTcUserAbortSentCount() {
        tcUserAbortSentCount.addAndGet(1);
    }

    @Override
    public long getInvokeLastReceivedCount() {
        return invokeLastReceivedCount.get();
    }

    public void updateInvokeLastReceivedCount() {
        invokeLastReceivedCount.addAndGet(1);
    }

    @Override
    public long getInvokeNotLastReceivedCount() {
        return invokeNotLastReceivedCount.get();
    }

    public void updateInvokeNotLastReceivedCount() {
        invokeNotLastReceivedCount.addAndGet(1);
    }

    @Override
    public long getInvokeLastSentCount() {
        return invokeLastSentCount.get();
    }

    public void updateInvokeLastSentCount() {
        invokeLastSentCount.addAndGet(1);
    }

    @Override
    public long getInvokeNotLastSentCount() {
        return invokeNotLastSentCount.get();
    }

    public void updateInvokeNotLastSentCount() {
        invokeNotLastSentCount.addAndGet(1);
    }

    @Override
    public long getReturnResultNotLastReceivedCount() {
        return returnResultNotLastReceivedCount.get();
    }

    public void updateReturnResultNotLastReceivedCount() {
        returnResultNotLastReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnResultNotLastSentCount() {
        return returnResultNotLastSentCount.get();
    }

    public void updateReturnResultNotLastSentCount() {
        returnResultNotLastSentCount.addAndGet(1);
    }

    @Override
    public long getReturnResultLastReceivedCount() {
        return returnResultLastReceivedCount.get();
    }

    public void updateReturnResultLastReceivedCount() {
        returnResultLastReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnResultLastSentCount() {
        return returnResultLastSentCount.get();
    }

    public void updateReturnResultLastSentCount() {
        returnResultLastSentCount.addAndGet(1);
    }

    @Override
    public long getReturnErrorReceivedCount() {
        return returnErrorReceivedCount.get();
    }

    public void updateReturnErrorReceivedCount() {
        returnErrorReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnErrorSentCount() {
        return returnErrorSentCount.get();
    }

    public void updateReturnErrorSentCount() {
        returnErrorSentCount.addAndGet(1);
    }

    @Override
    public long getRejectReceivedCount() {
        return rejectReceivedCount.get();
    }

    public void updateRejectReceivedCount() {
        rejectReceivedCount.addAndGet(1);
    }

    @Override
    public long getRejectSentCount() {
        return rejectSentCount.get();
    }

    public void updateRejectSentCount() {
        rejectSentCount.addAndGet(1);
    }

    @Override
    public long getDialogTimeoutCount() {
        return dialogTimeoutCount.get();
    }

    public void updateDialogTimeoutCount() {
        dialogTimeoutCount.addAndGet(1);
    }

    @Override
    public long getDialogReleaseCount() {
        return dialogReleaseCount.get();
    }

    public void updateDialogReleaseCount() {
        dialogReleaseCount.addAndGet(1);
    }


    @Override
    public long getCurrentDialogsCount() {
        return provider.getCurrentDialogsCount();
    }

    @Override
    public long getAllEstablishedDialogsCount() {
        return allEstablishedDialogsCount.get();
    }

    public void updateAllEstablishedDialogsCount() {
        allEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public long getAllLocalEstablishedDialogsCount() {
        return allLocalEstablishedDialogsCount.get();
    }

    public void updateAllLocalEstablishedDialogsCount() {
        allLocalEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public long getAllRemoteEstablishedDialogsCount() {
        return allRemoteEstablishedDialogsCount.get();
    }

    public void updateAllRemoteEstablishedDialogsCount() {
        allRemoteEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public Long getMinDialogsCount(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MIN_DIALOGS_COUNT, compainName);
        this.statDataCollection.updateData(MIN_DIALOGS_COUNT, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMinDialogsCount(long newVal) {
        this.statDataCollection.updateData(MIN_DIALOGS_COUNT, newVal);
    }

    @Override
    public Long getMaxDialogsCount(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_DIALOGS_COUNT, compainName);
        this.statDataCollection.updateData(MAX_DIALOGS_COUNT, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxDialogsCount(long newVal) {
        this.statDataCollection.updateData(MAX_DIALOGS_COUNT, newVal);
    }

    @Override
    public long getAllDialogsDuration() {
        return allDialogsDuration.get();
    }

    public void updateAllDialogsDuration(long diff) {
        allDialogsDuration.addAndGet(diff);
    }

    @Override
    public Map<String, LongValue> getOutgoingDialogsPerApplicatioContextName(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, compainName);
        this.statDataCollection.updateData(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingDialogsPerApplicatioContextName(String name) {
        this.statDataCollection.updateData(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, name);
    }

    @Override
    public Map<String, LongValue> getIncomingDialogsPerApplicatioContextName(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, compainName);
        this.statDataCollection.updateData(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingDialogsPerApplicatioContextName(String name) {
        this.statDataCollection.updateData(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingInvokesPerOperationCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_INVOKES_PER_OPERATION_CODE, compainName);
        this.statDataCollection.updateData(OUTGOING_INVOKES_PER_OPERATION_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingInvokesPerOperationCode(String name) {
        this.statDataCollection.updateData(OUTGOING_INVOKES_PER_OPERATION_CODE, name);
    }

    @Override
    public Map<String, LongValue> getIncomingInvokesPerOperationCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_INVOKES_PER_OPERATION_CODE, compainName);
        this.statDataCollection.updateData(INCOMING_INVOKES_PER_OPERATION_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingInvokesPerOperationCode(String name) {
        this.statDataCollection.updateData(INCOMING_INVOKES_PER_OPERATION_CODE, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingErrorsPerErrorCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_ERRORS_PER_ERROR_CODE, compainName);
        this.statDataCollection.updateData(OUTGOING_ERRORS_PER_ERROR_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingErrorsPerErrorCode(String name) {
        this.statDataCollection.updateData(OUTGOING_ERRORS_PER_ERROR_CODE, name);
    }

    @Override
    public Map<String, LongValue> getIncomingErrorsPerErrorCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_ERRORS_PER_ERROR_CODE, compainName);
        this.statDataCollection.updateData(INCOMING_ERRORS_PER_ERROR_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingErrorsPerErrorCode(String name) {
        this.statDataCollection.updateData(INCOMING_ERRORS_PER_ERROR_CODE, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingRejectPerProblem(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_REJECT_PER_PROBLEM, compainName);
        this.statDataCollection.updateData(OUTGOING_REJECT_PER_PROBLEM, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingRejectPerProblem(String name) {
        this.statDataCollection.updateData(OUTGOING_REJECT_PER_PROBLEM, name);
    }

    @Override
    public Map<String, LongValue> getIncomingRejectPerProblem(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_REJECT_PER_PROBLEM, compainName);
        this.statDataCollection.updateData(INCOMING_REJECT_PER_PROBLEM, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingRejectPerProblem(String name) {
        this.statDataCollection.updateData(INCOMING_REJECT_PER_PROBLEM, name);
    }

}
