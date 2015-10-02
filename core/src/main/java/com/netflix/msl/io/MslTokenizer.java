/**
 * Copyright (c) 2015 Netflix, Inc.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.msl.io;

/**
 * <p>A {@code MslTokenizer} takes in a binary source and parses out
 * {@link MslObject} and {@link MslArray} instances.</p>
 * 
 * @author Wesley Miaw <wmiaw@netflix.com>
 */
public abstract class MslTokenizer {
    /**
     * <p>Create a new tokenizer.</p>
     */
    protected MslTokenizer() {
        this.next = null;
    }
    
    /**
     * <p>Returns true if more objects can be read from the data source. This
     * method determines that by actually trying to read the next object.</p>
     * 
     * @param timeout read timeout in milliseconds.
     * @return true if more objects are available from the data source.
     * @throws MslEncoderException if the next object cannot be read or the
     *         source data at the current position is invalid.
     */
    public boolean more(final int timeout) throws MslEncoderException {
        if (next != null) return true;
        next = nextObject(timeout);
        return (next != null);
    }
    
    /**
     * <p>Return the next object (should be an instance of {@link MslObject} or
     * {@link MslArray}) from the source data.</p>
     * 
     * <p>If the source data's current position cannot be parsed as an object,
     * an exception is thrown and the source data position's new position is
     * undefined. Subsequent calls to this function should not re-throw the
     * exception and instead should look for the next object. The algorithm
     * used to search for the next object, and how the position should be set
     * to do so, is up to the implementer and may depend upon the encoding.</p>
     * 
     * @param timeout read timeout in milliseconds.
     * @return the next object or {@code null} if there are no more.
     * @throws MslEncoderException if the next object cannot be read or the
     *         source data at the current position is invalid.
     */
    protected abstract MslObject next(final int timeout) throws MslEncoderException;
    
    /**
     * <p>Return the next object.</p>
     * 
     * @param timeout read timeout in milliseconds.
     * @return the next object or {@code null} if there are no more.
     * @throws MslEncoderException if the next object cannot be read or the
     *         source data at the current position is invalid.
     */
    public MslObject nextObject(final int timeout) throws MslEncoderException {
        if (next != null) {
            final MslObject mo = next;
            next = null;
            return mo;
        }
        return next(timeout);
    }
    
    /** Cached next object. */
    private MslObject next;
}
