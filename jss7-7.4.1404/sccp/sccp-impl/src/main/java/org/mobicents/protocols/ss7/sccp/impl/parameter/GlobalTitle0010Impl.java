/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0010;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 */
public class GlobalTitle0010Impl extends AbstractGlobalTitle implements GlobalTitle0010 {

    private int translationType;

    public GlobalTitle0010Impl() {
        super.encodingScheme = BCDEvenEncodingScheme.INSTANCE;
    }

    /**
     * @param natureOfAddress
     */
    public GlobalTitle0010Impl(final String digits,final int translationType) {
        this();

        if(digits == null){
            throw new IllegalArgumentException();
        }
        this.translationType = translationType;
        super.digits = digits;
        super.encodingScheme = super.digits.length() % 2 == 1 ? BCDOddEncodingScheme.INSTANCE : BCDEvenEncodingScheme.INSTANCE;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
    }

    @Override
    public int getTranslationType() {
        return this.translationType;
    }

    @Override
    public void decode(final InputStream in,final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try{
        this.translationType = in.read() & 0xff;
        super.decode(in,factory, sccpProtocolVersion);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public void encode(final OutputStream out, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
//            boolean odd = (super.digits.length() % 2) != 0;
//            // encoding first byte
//            int b = 0x00;
//            if (odd) {
//                b = b | (byte) 0x80;
//            }
//            // adding nature of address indicator
//            b = b | (byte) this.translationType;
//            // write first byte
//            out.write((byte) b);
            // encode digits

            out.write(this.translationType);
            super.encode(out, removeSpc, sccpProtocolVersion);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public void decode(final byte[] b,final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        this.decode(new ByteArrayInputStream(b),factory, sccpProtocolVersion);
    }

    @Override
    public byte[] encode(final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos, removeSpc, sccpProtocolVersion);
        return baos.toByteArray();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + translationType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GlobalTitle0010Impl other = (GlobalTitle0010Impl) obj;
        if (translationType != other.translationType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GlobalTitle0010Impl [digits=" + digits + ", translationType=" + translationType + ", encodingScheme="
                + encodingScheme + "]";
    }

    protected static final XMLFormat<GlobalTitle0010Impl> XML = new XMLFormat<GlobalTitle0010Impl>(GlobalTitle0010Impl.class) {

        public void write(GlobalTitle0010Impl ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(TRANSLATION_TYPE, ai.translationType);
            xml.setAttribute(DIGITS, ai.digits);
        }

        public void read(InputElement xml, GlobalTitle0010Impl ai) throws XMLStreamException {
            ai.translationType = xml.getAttribute(TRANSLATION_TYPE).toInt();
            ai.digits = xml.getAttribute(DIGITS).toString();
        }
    };
}
