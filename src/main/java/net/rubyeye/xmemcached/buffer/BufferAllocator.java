/**
 *Copyright [2009-2010] [dennis zhuang(killme2008@gmail.com)]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *             http://www.apache.org/licenses/LICENSE-2.0 
 *Unless required by applicable law or agreed to in writing, 
 *software distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *either express or implied. See the License for the specific language governing permissions and limitations under the License
 */
package net.rubyeye.xmemcached.buffer;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * IoBuffer allocator
 *
 * @author dennis(killme2008@gmail.com)
 */
@Deprecated
public interface BufferAllocator extends Serializable {
    public IoBuffer allocate(int capacity);

    public IoBuffer wrap(ByteBuffer byteBuffer);

    public void dispose();
}
