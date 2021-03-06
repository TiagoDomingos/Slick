/*
 * Copyright 2018. M. Reza Nasirloo
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

package com.mrezanasirloo.slick.supportfragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks;

import com.mrezanasirloo.slick.InternalOnDestroyListener;
import com.mrezanasirloo.slick.SlickPresenter;
import com.mrezanasirloo.slick.SlickUniqueId;

import java.util.Locale;


/**
 * @author : M.Reza.Nasirloo@gmail.com
 * Created on: 2016-11-03
 */

public class SlickDelegateFragment<V, P extends SlickPresenter<V>> extends FragmentLifecycleCallbacks {

    private int id;
    private InternalOnDestroyListener listener;

    private P presenter;
    private Class cls;
    private boolean multiInstance = false;

    public SlickDelegateFragment(P presenter, Class cls, int id) {
        if (presenter == null) {
            throw new IllegalStateException("Presenter cannot be null.");
        }
        this.presenter = presenter;
        this.cls = cls;
        this.id = id;
        if (id != -1) multiInstance = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onFragmentStarted(FragmentManager fm, Fragment fragment) {
        if (multiInstance) {
            if (isSameInstance(fragment)) {
                presenter.onViewUp((V) fragment);
            }

        } else if (cls.isInstance(fragment)) {
            presenter.onViewUp((V) fragment);
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment fragment) {
        if (multiInstance) {
            if (isSameInstance(fragment)) {
                presenter.onViewDown();
            }
        } else if (cls.isInstance(fragment)) {
            presenter.onViewDown();
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment fragment) {
        if (multiInstance) {
            if (isSameInstance(fragment)) {
                destroy(fragment);
            }
        } else if (cls.isInstance(fragment)) {
            destroy(fragment);
        }
    }

    private void destroy(@NonNull Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null || !activity.isChangingConfigurations()) {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            }
            presenter.onDestroy();
            if (listener != null) {
                listener.onDestroy(id);
            }
            presenter = null;
        }
    }

    public P getPresenter() {
        return presenter;
    }

    public void setListener(InternalOnDestroyListener listener) {
        this.listener = listener;
    }


    @SuppressWarnings("ConstantConditions")
    public static int getId(@NonNull Object view) {
        if (view == null) throw new NullPointerException("Cannot get an Id from a null view." +
                " Are you sure you call the delegate methods at the right place?");
        if (view instanceof SlickUniqueId) {
            String uniqueId = ((SlickUniqueId) view).getUniqueId();
            if (uniqueId == null) throw new IllegalStateException(String.format(Locale.ENGLISH,
                    "Your View: %s has implemented SlickUniqueId but instead of returning an Id it returned null", view.getClass().toString()));

            return uniqueId.hashCode();
        }
        return -1;
    }

    private boolean isSameInstance(@NonNull Object view) {
        final int id = getId(view);
        return id != -1 && id == this.id;
    }
}
