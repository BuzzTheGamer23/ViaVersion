/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2022 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.viaversion.viaversion.unsupported;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class UnsupportedSoftwareImpl implements UnsupportedSoftware {

    private final String name;
    private final List<String> classNames;
    private final List<UnsupportedMethods> methods;
    private final String reason;

    public UnsupportedSoftwareImpl(String name, List<String> classNames, List<UnsupportedMethods> methods, String reason) {
        this.name = name;
        this.classNames = Collections.unmodifiableList(classNames);
        this.methods = Collections.unmodifiableList(methods);
        this.reason = reason;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public boolean findMatch() {
        for (String className : classNames) {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException ignored) {
            }
        }
        for (UnsupportedMethods method : methods) {
            if (method.findMatch()) {
                return true;
            }
        }
        return false;
    }

    public static final class Builder {

        private final List<String> classNames = new ArrayList<>();
        private final List<UnsupportedMethods> methods = new ArrayList<>();
        private String name;
        private String reason;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder addMethod(String className, String methodName) {
            methods.add(new UnsupportedMethods(className, Collections.singleton(methodName)));
            return this;
        }

        public Builder addMethods(String className, String... methodNames) {
            methods.add(new UnsupportedMethods(className, new HashSet<>(Arrays.asList(methodNames))));
            return this;
        }

        public Builder addClassName(String className) {
            classNames.add(className);
            return this;
        }

        public UnsupportedSoftware build() {
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(reason);
            return new UnsupportedSoftwareImpl(name, classNames, methods, reason);
        }
    }

    public static final class Reason {

        public static final String DANGEROUS_SERVER_SOFTWARE = "You are using server software that - outside of possibly breaking ViaVersion - can also cause severe damage to your server's integrity as a whole.";
    }
}
