package com.shawn.taskassistant.support.classification.registry;

import com.shawn.taskassistant.support.failure.FailureSemantic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregates (normalizes) all per-port mapping tables into one lookup table.
 *
 * Key = (portFailureType, reasonEnumValue)
 *
 * Notes:
 * - reason is kept as Object to support different per-port reason enums without a shared interface.
 * - This registry performs ONLY aggregation + lookup. It does NOT infer semantics or inspect causes.
 */
public final class PortFailureSemanticRegistry {

    private final Map<MappingKey, FailureSemantic> table;

    private PortFailureSemanticRegistry(Map<MappingKey, FailureSemantic> table) {
        this.table = Collections.unmodifiableMap(new HashMap<>(table));
    }

    public FailureSemantic lookup(Class<?> portFailureType, Object reason) {
        Objects.requireNonNull(portFailureType, "portFailureType");
        Objects.requireNonNull(reason, "reason");
        return table.get(new MappingKey(portFailureType, reason));
    }

    public Map<MappingKey, FailureSemantic> snapshot() {
        return table;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<MappingKey, FailureSemantic> m = new HashMap<>();

        /**
         * Register a per-port mapping table.
         *
         * @param portFailureType concrete PortFailure class
         * @param perPortTable    Map<ReasonEnum, FailureSemantic>
         */
        public Builder register(Class<?> portFailureType, Map<?, FailureSemantic> perPortTable) {
            Objects.requireNonNull(portFailureType, "portFailureType");
            Objects.requireNonNull(perPortTable, "perPortTable");

            for (Map.Entry<?, FailureSemantic> e : perPortTable.entrySet()) {
                Object reason = Objects.requireNonNull(e.getKey(), "reason");
                FailureSemantic semantic = Objects.requireNonNull(e.getValue(), "semantic");

                MappingKey k = new MappingKey(portFailureType, reason);
                FailureSemantic prev = m.putIfAbsent(k, semantic);

                // duplicate entry with different target semantic is a bug -> fail-fast
                if (prev != null && prev != semantic) {
                    throw new IllegalStateException(
                        "Conflicting semantic mapping for key=" + k + ", prev=" + prev + ", now=" + semantic
                    );
                }
            }
            return this;
        }

        /**
         * Fail-fast if a given reason set is not fully covered.
         */
        public Builder requireCoverage(Class<?> portFailureType, Set<?> allReasons) {
            Objects.requireNonNull(portFailureType, "portFailureType");
            Objects.requireNonNull(allReasons, "allReasons");

            for (Object r : allReasons) {
                MappingKey k = new MappingKey(portFailureType, r);
                if (!m.containsKey(k)) {
                    throw new IllegalStateException(
                        "Missing semantic mapping for portFailureType=" + portFailureType.getSimpleName() + ", reason=" + r
                    );
                }
            }
            return this;
        }

        public PortFailureSemanticRegistry build() {
            return new PortFailureSemanticRegistry(m);
        }
    }
}