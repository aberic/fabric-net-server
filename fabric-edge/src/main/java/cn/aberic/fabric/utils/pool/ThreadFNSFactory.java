/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.utils.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 新建一个线程工场类
 * 
 * @author 杨毅
 *
 * @date 2017年10月18日 - 下午7:41:00
 * @email abericyang@gmail.com
 */
class ThreadFNSFactory implements ThreadFactory {

	private final String mName;
	private final AtomicInteger mNumber = new AtomicInteger();

	public ThreadFNSFactory(String name) {
		mName = name;// 线程的名称
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, mName + "-" + mNumber.getAndIncrement()) {
			@Override
			public void run() {
				super.run();
			}
		};
	}

}