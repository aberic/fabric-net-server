package cn.aberic.fabric.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Api {

    /** API 意图 */
    public enum Intent {
        INVOKE(1), QUERY(2), INFO(3), HASH(4), NUMBER(5), TXID(6);

        private int index;

        private Intent(int index) {
            this.index = index;
        }

        public static Intent get(int index) {
            for (Intent i : Intent.values()) {
                if (i.getIndex() == index) {
                    return i;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /** 接口名称 */
    public String name = "";
    /** 接口意图 */
    public int index = 0;
    /** 接口执行参数 */
    public String exec = "";

    public Api() {
    }

    public Api(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
