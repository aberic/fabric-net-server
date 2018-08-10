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

package cn.aberic.fabric.dao.mapper;

import cn.aberic.fabric.dao.Block;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 作者：Aberic on 2018/8/9 21:01
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface BlockMapper {

    @Insert("insert into block (channel_id,data_hash,calculated_hash,previous_hash," +
            "envelope_count,tx_count,r_w_set_count,timestamp,calculate_date,create_date) " +
            "values " +
            "(#{b.channelId},#{b.dataHash},#{b.calculatedHash},#{b.previousHash},#{b.envelopeCount}," +
            "#{b.txCount},#{b.rwSetCount},#{b.timestamp},#{b.calculateDate},#{b.createDate})")
    int add(@Param("b")Block block);

    @Select("select count(id) from block")
    int count();

    @Select("select count(id) from block where channel_id=#{channelId}")
    int countByChannelId(@Param("channelId") int channelId);

    @Select("select count(id) from block where calculate_date=#{calculateDate}")
    int countByDate(@Param("calculateDate") int calculateDate);

    @Select("select count(id) from block where channel_id=#{channelId} and calculate_date=#{calculateDate}")
    int countByChannelIdAndDate(@Param("channelId") int channelId, @Param("calculateDate") int calculateDate);

    @Select("select sum(tx_count) from block")
    int countTx();

    @Select("select sum(tx_count) from block where calculate_date=#{calculateDate}")
    int countTxByDate(@Param("calculateDate") int calculateDate);

    @Select("select sum(tx_count) from block where channel_id=#{channelId}")
    int countTxByChannelId(@Param("channelId") int channelId);

    @Select("select sum(r_w_set_count) from block")
    int countRWSet();

    @Select("select sum(r_w_set_count) from block where channel_id=#{channelId}")
    int countRWSetByChannelId(@Param("channelId") int channelId);

}
