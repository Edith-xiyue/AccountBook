package com.example.accountbook.tools;

import com.example.accountbook.database.table.IncomeEntity;

public class EventBusConfig {

    public static class WarningView {

        private boolean warningViewShow;

        public boolean isWarningViewShow() {
            return warningViewShow;
        }

        public void setWarningViewShow(boolean warningViewShow) {
            this.warningViewShow = warningViewShow;
        }
    }

    public static class alterIncomeEntitie{
        public IncomeEntity entity;

        public alterIncomeEntitie(IncomeEntity entity) {
            this.entity = entity;
        }

        public IncomeEntity getEntity() {
            return entity;
        }
    }

    public static class deleteIncomeEntitie{
    }
}
