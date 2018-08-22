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

import cn.aberic.fabric.dao.entity.Block;
import cn.aberic.fabric.dao.provider.BlockDAOProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/8/9 21:01
 * 邮箱：abericyang@gmail.com
 */
@Mapper
public interface BlockMapper {

    @Insert("insert into fns_block (channel_id,height,data_hash,calculated_hash,previous_hash," +
            "envelope_count,tx_count,r_w_set_count,timestamp,calculate_date,create_date) " +
            "values " +
            "(#{b.channelId},#{b.height},#{b.dataHash},#{b.calculatedHash},#{b.previousHash},#{b.envelopeCount}," +
            "#{b.txCount},#{b.rwSetCount},#{b.timestamp},#{b.calculateDate},#{b.createDate})")
    int add(@Param("b")Block block);

    @InsertProvider(type = BlockDAOProvider.class, method = "insertAll")
    int addList(@Param("list") List<Block> blocks);

    @Select("select count(id) from fns_block")
    int count();

    @Select("select count(id) from fns_block where channel_id=#{channelId}")
    int countByChannelId(@Param("channelId") int channelId);

    @Select("select count(id) from fns_block where calculate_date=#{calculateDate}")
    int countByDate(@Param("calculateDate") int calculateDate);

    @Select("select count(id) from fns_block where channel_id=#{channelId} and calculate_date=#{calculateDate}")
    int countByChannelIdAndDate(@Param("channelId") int channelId, @Param("calculateDate") int calculateDate);

    @Select("select sum(tx_count) from fns_block")
    int countTx();

    @Select("select sum(tx_count) from fns_block where calculate_date=#{calculateDate}")
    int countTxByDate(@Param("calculateDate") int calculateDate);

    @Select("select sum(tx_count) from fns_block where channel_id=#{channelId}")
    int countTxByChannelId(@Param("channelId") int channelId);

    @Select("select sum(r_w_set_count) from fns_block")
    int countRWSet();

    @Select("select sum(r_w_set_count) from fns_block where calculate_date=#{calculateDate}")
    int countRWSetByDate(@Param("calculateDate") int calculateDate);

    @Select("select channel_id,height,data_hash,calculated_hash,previous_hash,envelope_count,tx_count,r_w_set_count," +
            "timestamp,calculate_date,create_date from fns_block where channel_id=#{channelId} order by id desc limit 1")
    @Results({
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "dataHash", column = "data_hash"),
            @Result(property = "calculatedHash", column = "calculated_hash"),
            @Result(property = "previousHash", column = "previous_hash"),
            @Result(property = "envelopeCount", column = "envelope_count"),
            @Result(property = "txCount", column = "tx_count"),
            @Result(property = "rwSetCount", column = "r_w_set_count"),
            @Result(property = "calculateDate", column = "calculate_date"),
            @Result(property = "createDate", column = "create_date")
    })
    Block getByChannelId(@Param("channelId") int channelId);

    @Select("select channel_id,height,data_hash,calculated_hash,previous_hash,envelope_count,tx_count,r_w_set_count," +
            "timestamp,calculate_date,create_date from fns_block where channel_id=#{channelId} order by id desc limit #{limit}")
    @Results({
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "dataHash", column = "data_hash"),
            @Result(property = "calculatedHash", column = "calculated_hash"),
            @Result(property = "previousHash", column = "previous_hash"),
            @Result(property = "envelopeCount", column = "envelope_count"),
            @Result(property = "txCount", column = "tx_count"),
            @Result(property = "rwSetCount", column = "r_w_set_count"),
            @Result(property = "calculateDate", column = "calculate_date"),
            @Result(property = "createDate", column = "create_date")
    })
    List<Block> getLimit(@Param("channelId") int channelId, @Param("limit") int limit);

    @Delete("delete from fns_block where channel_id=#{channelId}")
    int deleteByChannelId(@Param("channelId") int channelId);

}
