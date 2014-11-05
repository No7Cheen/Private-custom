/*
 * Copyright 2014 Flavio Faria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pc.ui.flaviofaria.kenburnsview.libs;

import com.pc.ui.flaviofaria.kenburnsview.KenBurnsView;

import android.graphics.RectF;

public interface KenBurnsViewTransitionGenerator {

	/**
	 * Generates the next transition to be played by the {@link KenBurnsView}.
	 * @param drawableBounds the bounds of the drawable to be shown in the {@link KenBurnsView}.
	 * @param viewport the rect that represents the viewport where
	 *                 the transition will be played in. This is usually the bounds of the
	 *                 {@link KenBurnsView}.
	 * @return a {@link KenBurnsViewTransition} object to be played by the {@link KenBurnsView}.
	 */
	public KenBurnsViewTransition generateNextTransition(RectF drawableBounds, RectF viewport);

}
